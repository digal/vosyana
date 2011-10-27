package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 08.04.2008
 * Time: 12:20:07
 */
public interface IONNNEntry {
    int getId();
    String getTargetId();
    String getVoterId();
    String getChatId();
    long getTimestamp();
}
