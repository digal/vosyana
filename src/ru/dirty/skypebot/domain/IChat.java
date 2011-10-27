package ru.dirty.skypebot.domain;

import com.skype.SkypeException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 15:49:08
 */
public interface IChat extends ISkypeEntity{
    public boolean isResponseEnabled();
    public void setResponseEnabled(boolean enabled);
    public boolean isONNNEnabled();
    public boolean isDefault();


    Tag addTag(String tag, String user) throws IllegalAccessException;

    List<Tag> getAllTags();

    String getTagsString();

    int getExitCost();

    void setExitCost(int exitCost);

    int userExit(String userId);

    void addFeed(Feed f) throws Exception;

    void addFeed(String url, String usr) throws Exception;

}
