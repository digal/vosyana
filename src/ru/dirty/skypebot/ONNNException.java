package ru.dirty.skypebot;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 11.04.2008
 * Time: 17:18:31
 */
public class ONNNException extends Exception {
    private String voter;
    private String target;
    private String chat;

    public ONNNException() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public ONNNException(String voter, String target, String chat) {
        super();
        this.voter = voter;
        this.target = target;
        this.chat = chat;
    }

    public ONNNException(String voter, String target, String chat, String message) {
        super(message);
        this.voter = voter;
        this.target = target;
        this.chat = chat;
    }

    public String toString() {
        return this.getClass().getName()
                + ": "+getLocalizedMessage()
                + "\n voter="+voter
                + "\n target="+target
                + "\n chat="+chat; 
    }
}
