package ru.dirty.skypebot;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;
import ru.dirty.skypebot.logging.Logger;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 17:52:23
 */
public class MessageProcessor implements Runnable, ChatMessageListener {

    private final ArrayList<ChatMessage> messages;
    private final Lock lock = new ReentrantLock();
    private final Condition msgInList = lock.newCondition();
    private final Condition emptyList = lock.newCondition();

    public void run() {
        while (true) {
            //Logger.getInstance().log("Locking...");
            lock.lock();
            try {
                while (messages.size() == 0) {
                    //Logger.getInstance().log("Awaiting for messages in list...");
                    msgInList.await();
                    //Logger.getInstance().log("Continuing");
                }
                if (messages.size() > 1) {
                    //Logger.getInstance().log("Messages in queue: "+messages.size());
                }
                for (ChatMessage message: messages) {
                    //Logger.getInstance().log("Message processor, processing message: "+message.getId());
                    CommandGetter.getInstance().processRecievedMessage(message);
                    //Logger.getInstance().log("Message processed: "+message.getId());
                }
                messages.clear();
                //Logger.getInstance().log("Signaling empty list");
                emptyList.signal();
            } catch (InterruptedException ex) {
               ex.printStackTrace();
            } finally {
               //Logger.getInstance().log("Unlocking");
               lock.unlock();
            }
        }
    }

    public MessageProcessor() {
        messages = new ArrayList<ChatMessage>();
    }

    public void chatMessageReceived(ChatMessage chatMessage) throws SkypeException {
        //Logger.getInstance().log("Message recieved: "+chatMessage.getId()+" in chat "+chatMessage.getChat().getId());
        lock.lock();
        try {
            while (messages.size()>0) {
                //Logger.getInstance().log("Messages list size > 0, awaiting");
                emptyList.await();
                //Logger.getInstance().log("Awaiting complete");
            }
            //Logger.getInstance().log("Adding message "+chatMessage.getId()+" to list.");
            messages.add(chatMessage);
            msgInList.signal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void chatMessageSent(ChatMessage chatMessage) throws SkypeException {
        if (chatMessage.getContent().startsWith("!")) {
            lock.lock();
            try {
                while (messages.size()>0)
                emptyList.await();
                messages.add(chatMessage);
                msgInList.signal();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
