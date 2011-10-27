package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 15:32:41
 */
public class BaseDomain implements IBaseDomain {
    private int id ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int hashCode() {
        int hash = 12;
        hash = 31 * hash + id;
        hash = 31 * hash + getClass().getName().hashCode();
        return hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this.getClass() == obj.getClass()) {
            IBaseDomain d2 = (IBaseDomain) obj;
            if (getId() !=0 || d2.getId() != 0) {
                return getId() == d2.getId();
            } else {
                return this == d2;
            }
        } else {
            return super.equals(obj);        
        }

    }
}
