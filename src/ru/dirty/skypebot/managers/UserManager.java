/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 15:52:57
 */
package ru.dirty.skypebot.managers;

import ru.dirty.skypebot.domain.IUser;
import ru.dirty.skypebot.storage.DBManager;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public class UserManager extends AbstractSkypeManager<IUser> {
    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private UserManager() {
        super();
    }

    protected IUser getFromDB(String skypeId) throws SQLException {
        return DBManager.getInstance().getUser(skypeId);
    }

    protected List<IUser> getAllFromDB() throws SQLException {
        return DBManager.getInstance().getUsers();
    }

    public boolean userExists(String skypeId) {
        return cacheContains(skypeId);
    }

    public IUser addUser(String skypeId, String role) throws SQLException {
        IUser usr = DBManager.getInstance().addUser(skypeId, role);
        addToCache(usr);
        return usr;
    }

    public IUser addUser(String skypeId) throws SQLException {
        return addUser(skypeId, IUser.ROLE_USER);
    }

    public List<IUser> getAdmins() throws SQLException {
        return DBManager.getInstance().getAdmins();
    }

    public void setRole(String id, String role) throws SQLException {
        IUser u = get(id);
        if (u==null) {
            addUser(id, role);
        } else {
            u.setRole(role);
            DBManager.getInstance().updateUser(u);
        }
        
    }


}
