/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 16:07:11
 */
package ru.dirty.skypebot.managers;

import ru.dirty.skypebot.domain.IChat;
import ru.dirty.skypebot.storage.DBManager;

import java.util.List;
import java.sql.SQLException;

public class ChatManager extends AbstractSkypeManager<IChat>{
    private static ChatManager ourInstance = new ChatManager();

    public static ChatManager getInstance() {
        return ourInstance;
    }

    private ChatManager() {
        super();
    }

    protected IChat getFromDB(String skypeId) throws Exception {
        return DBManager.getInstance().getChat(skypeId);
    }

    protected List<IChat> getAllFromDB() throws Exception {
        return DBManager.getInstance().getChats();
    }

    public boolean chatExists(String skypeId) {
        return cacheContains(skypeId);
    }

    public IChat addChat(String skypeId) throws Exception {
        IChat ch = DBManager.getInstance().addChat(skypeId, true, true);
        addToCache(ch);
        return ch;
    }

    public void setChatEnabledState(String skypeId, boolean enabled) throws SQLException {
        IChat ch = get(skypeId);
        if (ch!=null) {
            ch.setResponseEnabled(enabled);
            DBManager.getInstance().updateChat(ch);
        }
    }

    public boolean setChatExitCost(String skypeId, int cost) throws SQLException {
        IChat ch = get(skypeId);
        if (ch!=null) {
            ch.setExitCost(cost);
            //DBManager.getInstance().updateChat(ch);
            return true;
        }
        return false; 
    }

}
