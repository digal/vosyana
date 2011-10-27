package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 15:43:17
 */
public interface IUser extends ISkypeEntity{

    /**
     * Apelsinized user.
     */
    public static final String ROLE_ORANGE = "orange";

    /**
     * Regular user.
     */
    public static final String ROLE_USER = "user";

    /**
     * Bot admin.
     */
    public static final String ROLE_ADMIN = "admin";

    /**
     * Bot master.
     */
    public static final String ROLE_MASTER = "master";

    /**
     * Gets user role. 
     * @return user role String.
     */
    public String getRole();

    public void setRole(String role);
}
