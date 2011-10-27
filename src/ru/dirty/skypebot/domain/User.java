package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 15:31:37
 */
public class User extends SkypeEntity implements IUser{
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
