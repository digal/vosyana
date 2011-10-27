package ru.dirty.skypebot.domain;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 11.04.2008
 * Time: 13:38:01
 */
public abstract class SkypeEntity{
    private String skypeId;

    public String getSkypeId() {
        return skypeId;
    }

    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    public int hashCode() {
        return skypeId.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj!=null && this.getClass().equals(obj.getClass())) {
            ISkypeEntity e2 = (ISkypeEntity) obj;
            return this.getSkypeId().equals(e2.getSkypeId());
        } else {
            return super.equals(obj);
        }
    }
}
