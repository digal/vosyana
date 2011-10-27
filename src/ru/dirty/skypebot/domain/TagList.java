package ru.dirty.skypebot.domain;

import ru.dirty.skypebot.logging.Logger;
import ru.dirty.skypebot.services.TwitterService;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: May 11, 2009
 * Time: 10:59:07 PM
 */
public class TagList {
    private List<Tag> tags;
    private String chat;
    private Timer cleanupTimer;

    //24 hrs
    public static final long TAG_TTL = 1000*60*60*24;

    //10 mins
    public static final long CLEANUP_PERIOD = 1000*60*10;

    //1 min
    public static final long ONE_USER_PERIOD = 1000*60*1;

    public TagList(String chat) {
        this.chat = chat;
        tags = new ArrayList<Tag>();
        cleanupTimer = new Timer(true);
        TimerTask cleanupTask = new TimerTask(){
            public void run() {
                clear();
            }
        };
        cleanupTimer.schedule(cleanupTask, CLEANUP_PERIOD, CLEANUP_PERIOD);
    }

    private void clear() {
        long time = System.currentTimeMillis();
        List<Tag> newTags = new ArrayList<Tag>();
        int removed = 0;
        synchronized (this) {
            for (Tag t : tags) {
                if ((time-t.getTimestamp()) < TAG_TTL) {
                    newTags.add(t);
                } else {
                    removed++;
                }
            }
            tags = newTags;
        }
        if (removed > 0) {
            Logger.getInstance().log("Tag cleanup performed for " + chat+". Tags removed: "+removed);
        }

    }

    public Tag addTag(String tag, String user) throws IllegalAccessException {
        long time = System.currentTimeMillis();
        Tag t = new Tag(tag, user);
        boolean twit = true;
        for (Tag oldTag : tags) {
            if (oldTag.getContent().equalsIgnoreCase(t.getContent())) {
                tags.remove(oldTag);
                twit = false;
                break;
            }
        }
        synchronized (this) {
            if (tags.size() > 0) {
                Tag lastTag = tags.get(tags.size() -1);
                if (lastTag.getUser().equals(user) && (time - lastTag.getTimestamp()) < ONE_USER_PERIOD) {
                    Logger.getInstance().log("Tag was not added to chat due to user period violation" + chat);
                    throw new IllegalAccessException("You should wait a little to add a new tag, " + user);
                }
            }
            tags.add(t);
            if (twit) {
              TwitterService.getInstance().send(t.getContent());
            }
        }
        Logger.getInstance().log("Added tag from user "+user+" for chat " + chat);
        return t;
    }

    public synchronized List<Tag> getAllTags() {
        return new ArrayList<Tag>(tags);
    }



}
