#include <stdio.h>
#include <malloc.h>
#include <string.h>

#include <X11/Xlib.h>
#include <X11/Xatom.h>

#include "com_skype_connector_linux_SkypeFramework.h"

#define SKYPE_FRAMEWORK_CLASS "com/skype/connector/linux/SkypeFramework"

#define SKYPE_STRING_MAX 0x10000

static Display *_display = NULL;
static int _screen = -1;
static Window _desktop;
static Window _dummyWindow;

static Atom _skypeInstanceAtom;
static Atom _skypeControlApiMessageBeginAtom;
static Atom _skypeControlApiMessageAtom;
static Atom _stopEventLoopAtom;

static Bool _dispatching;
static Window _skypeWindow;

static void throwInternalError(JNIEnv *env, char *message) {
	jclass clazz = (*env)->FindClass(env, "java/lang/InternalError");
	(*env)->ThrowNew(env, clazz, message);
}

static Bool checkNull(JNIEnv *env, void *value) {
	if (value == NULL) {
		jclass clazz = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
		(*env)->ThrowNew(env, clazz, NULL);
		return True;
	} else {
		return False;
	}
}

JNIEXPORT void JNICALL Java_com_skype_connector_linux_SkypeFramework_setup0(JNIEnv *env, jclass this) {
	if (XInitThreads() == 0) {
		throwInternalError(env, "Xlib don't support multi-threads.");
		return;
	}

	_display = XOpenDisplay(NULL);
	if (_display == NULL) {
		throwInternalError(env, "Opening the diplay failed.");
		return;
	}
	
	_screen = DefaultScreen(_display);
	_desktop = XRootWindow(_display, _screen);
	_dummyWindow = XCreateSimpleWindow(_display, _desktop, 0, 0, 1, 1, 0, BlackPixel(_display, _screen), BlackPixel(_display, _screen));
	
	_skypeInstanceAtom = XInternAtom(_display, "_SKYPE_INSTANCE", False);
	_skypeControlApiMessageBeginAtom = XInternAtom(_display, "SKYPECONTROLAPI_MESSAGE_BEGIN", False);
	_skypeControlApiMessageAtom = XInternAtom(_display, "SKYPECONTROLAPI_MESSAGE", False);
	_stopEventLoopAtom = XInternAtom(_display, "_STOP_EVENT_LOOP", False);
	
	_dispatching = True;
}

static void fireNotificationReceived(JNIEnv *env, char *notificationChars) {
	jstring notificationString = (*env)->NewStringUTF(env, notificationChars);
	if (checkNull(env, (void *)notificationString)) {
		return;
	}

	jclass clazz  = (*env)->FindClass(env, SKYPE_FRAMEWORK_CLASS);
	jmethodID method = (*env)->GetStaticMethodID(env, clazz, "fireNotificationReceived", "(Ljava/lang/String;)V");
	(*env)->CallStaticVoidMethod(env, clazz, method, notificationString);
}

JNIEXPORT void JNICALL Java_com_skype_connector_linux_SkypeFramework_runEventLoop0(JNIEnv *env, jclass this) {
	XEvent event;
	char buffer[21];
	char *notificationChars = (char *)malloc(sizeof(char) * SKYPE_STRING_MAX);
	notificationChars[0] = '\0';

	while(True) {
		XNextEvent(_display, &event);
		if (event.type == ClientMessage) {
			if (event.xclient.message_type == _stopEventLoopAtom) {
				break;
			}
			
			if (event.xclient.format != 8)
				continue;

			int i;
			for (i = 0; i < 20 && event.xclient.data.b[i] != '\0'; i++) {
				buffer[i] = event.xclient.data.b[i];
			}
			buffer[i] ='\0';
			strcat(notificationChars, buffer);
			if (i < 20) {
				fireNotificationReceived(env, notificationChars);
				notificationChars[0] = '\0';
			}
		}
	}
}

static Window getSkypeWindow() {
	Atom actualType;
	int actualFormat;
	unsigned long numberOfItems;
	unsigned long bytesAfter;
	unsigned char *data;
	int status;

	status = XGetWindowProperty(_display, _desktop, _skypeInstanceAtom, 0, 1, False, XA_WINDOW, &actualType, &actualFormat, &numberOfItems, &bytesAfter, &data);

	if (status != Success || &actualType == None || actualFormat != 32 || numberOfItems != 1) {
		return None;
	} else {
		_skypeWindow = *(Window *)data;
		return _skypeWindow;
	}
}

static Bool isRunning() {
	return getSkypeWindow() != None;
}

JNIEXPORT jboolean JNICALL Java_com_skype_connector_linux_SkypeFramework_isRunning0(JNIEnv *env, jclass this) {
	return isRunning()? JNI_TRUE: JNI_FALSE;
}

static void sendCommand(const char *commandChars) {
	if (!isRunning()) {
		return;
	}

	unsigned int position = 0;
	unsigned int length = strlen(commandChars);

	XEvent event;
	memset(&event, 0, sizeof(XEvent));
	event.xclient.type = ClientMessage;
	event.xclient.message_type = _skypeControlApiMessageBeginAtom;
	event.xclient.display = _display;
	event.xclient.window = _dummyWindow;
	event.xclient.format = 8;

	do {
		int i;
		for (i = 0; i < 20 && i + position <= length; i++) {
			event.xclient.data.b[i] = commandChars[i + position];
		}
		XSendEvent(_display, _skypeWindow, False, 0, &event);

		event.xclient.message_type = _skypeControlApiMessageAtom;
		position += i;
	} while (position <= length);
	XFlush(_display);
}

JNIEXPORT void JNICALL Java_com_skype_connector_linux_SkypeFramework_sendCommand0(JNIEnv *env, jclass this, jstring command) {
	jboolean isCopy;
	const char *commandChars = (*env)->GetStringUTFChars(env, command, &isCopy);
	sendCommand(commandChars);
	if (isCopy == JNI_TRUE) {
		(*env)->ReleaseStringUTFChars(env, command, commandChars);
	}
}

JNIEXPORT void JNICALL Java_com_skype_connector_linux_SkypeFramework_stopEventLoop0(JNIEnv *env, jclass this) {
	XEvent event;
	memset(&event, 0, sizeof(XEvent));
	event.xclient.type = ClientMessage;
	event.xclient.message_type = _stopEventLoopAtom;
	event.xclient.display = _display;
	event.xclient.window = _dummyWindow;
	event.xclient.format = 8;

	XSendEvent(_display, _dummyWindow, False, 0, &event);
	XFlush(_display);
}

JNIEXPORT void JNICALL Java_com_skype_connector_linux_SkypeFramework_closeDisplay0(JNIEnv *env, jclass this) {
	if (_display != NULL) {
		XCloseDisplay(_display);
		_display = NULL;
	}
}
