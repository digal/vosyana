/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 17:12:08
 */
package ru.dirty.skypebot.logging;

public class Logger {
    private static Logger ourInstance = new Logger();

    public static Logger getInstance() {
        return ourInstance;
    }

    private Logger() {
    }

    public void log(String message) {
        System.out.println(""+System.currentTimeMillis()+" #"+Thread.currentThread().getId()+": " + message);
    }

    public void log(String message, Throwable ex) {
        log(message);
        ex.printStackTrace(System.out);
    }

}
