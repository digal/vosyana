package ru.dirty.skypebot.domain;

import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.skype.*;
import com.skype.Chat;

import java.sql.Timestamp;
import java.net.URL;
import java.io.IOException;
import java.util.*;

import ru.dirty.skypebot.util.FIFOList;
import ru.dirty.skypebot.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: Sep 4, 2009
 * Time: 12:10:06 AM
 */
public class Feed {



    private int id;
    private String chatId;
    private String userId;
    private URL url;
    private int fetchInterval;
    private int showInterval;
    private Timestamp lastShownTs;
    private static SyndFeedInput ourInput = new SyndFeedInput();
    private SyndFeed feed;
    private final FIFOList<SyndEntry> entryCache = new FIFOList<SyndEntry>();
    private com.skype.Chat chat;

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUrlString() {
        return url.toExternalForm();

    }

    public int getFetchInterval() {
        return fetchInterval;
    }

    public int getShowInterval() {
        return showInterval;
    }

    public Timestamp getLastShownTs() {
        return lastShownTs;
    }

    public Feed(int id, String chatId, String userId, String url, int fetchInterval, int showInterval, Timestamp lastShownTs) throws IOException, FeedException {
        this.id = id;
        this.chatId = chatId;
        this.userId = userId;
        this.url = new URL(url);
        this.fetchInterval = fetchInterval;
        this.showInterval = showInterval;
        this.lastShownTs = lastShownTs;
        feed = ourInput.build(new XmlReader(this.url));

    }

    public void update() throws Exception {
        Logger.getInstance().log("Feed update "+url+" for chat "+chatId);
        int oldCacheSize = entryCache.size();
        List<SyndEntry> entries = feed.getEntries();
        Logger.getInstance().log("Entries recieved: "+entries.size());
        //assert entryCache!=null;
        if (entries!=null) {
            synchronized (entryCache) {
                for (SyndEntry entry : entries) {
                    assert entry!=null;
                    Date d = entry.getPublishedDate(); 
                    if (entry.getPublishedDate()==null) {
                        Logger.getInstance().log("!!!Entry date is null!!!");
                        continue;
                    }
                    
                    if ((!entryCache.contains(entry))
                            && d.getTime() > lastShownTs.getTime()) {
                        entryCache.add(entry);
                    }
                }
            }
            Logger.getInstance().log("Entries before update: "+oldCacheSize+"; after update: "+entryCache.size());
            if (oldCacheSize==0) {
                showNext(chat);
            }
        }
    }

    public void showNext(com.skype.Chat chat) throws SkypeException {
        Logger.getInstance().log("showNext Feed "+url+" for chat "+chatId+"; entryCache.size = "+entryCache.size());
        synchronized (entryCache) {
            if (entryCache.size() > 0) {
                SyndEntry next = entryCache.remove();
                Logger.getInstance().log("Feed "+url+" entry \""+next.getTitle()+"\" show for chat "+chatId);
                String text = formatEntry(next);
                lastShownTs = new Timestamp(next.getPublishedDate().getTime());
                chat.send(text);
            }
        }

    }

    private String formatEntry(SyndEntry next) {
        StringBuffer c = new StringBuffer();
        for (Object cont : next.getContents()) {
            c.append(((SyndContent)cont).getValue());
        }
        return next.getTitle()+"\n"+c+"\nLink: "+next.getLink();
    }

    public void activate() {
        TimerTask fetchTask = new TimerTask(){
            public void run() {
                try {
                    update();
                } catch (Exception e) {
                    Logger.getInstance().log("Feed "+url+" update exception.", e);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        TimerTask showTask = new TimerTask(){

            public void run() {
                try {
                    showNext(chat);
                } catch (Exception e) {
                    Logger.getInstance().log("Feed "+url+" for chat "+chatId+" show exception.", e);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };
        Timer fetchTimer = new Timer("fetchTimer");
        fetchTimer.schedule(fetchTask, 0, fetchInterval*1000);
        Timer showTimer = new Timer("showTimer");
        showTimer.schedule(showTask, showInterval*1000, showInterval*1000);

    }
}
