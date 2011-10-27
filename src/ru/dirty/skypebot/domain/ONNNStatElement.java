package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 12:41:33
 */
public class ONNNStatElement implements IONNNStatElement{
    private int votes;
    private String target;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
