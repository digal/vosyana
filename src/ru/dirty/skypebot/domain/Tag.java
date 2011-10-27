package ru.dirty.skypebot.domain;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: May 11, 2009
 * Time: 10:55:51 PM
 */
public class Tag {
    private String content;
    private String user;
    private long timestamp;

    public static final String TAG_SUFFIX = "[x]";

    public Tag(String content, String user) {
        this.content = content;
        this.user = user;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public String getUser() {
        return user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static String getTagContent(String rawTag) {
        String content = rawTag.substring(0, rawTag.lastIndexOf(TAG_SUFFIX));
        content = content.trim();
        return content;
    }

    public static boolean isTagString(String rawTag) {
        return (rawTag.trim().endsWith(TAG_SUFFIX));
    }


}


