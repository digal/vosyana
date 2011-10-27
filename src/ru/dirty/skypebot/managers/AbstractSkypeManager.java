package ru.dirty.skypebot.managers;

import ru.dirty.skypebot.domain.ISkypeEntity;

import java.util.HashMap;
import java.util.List;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Yuri.Buyanov
 * Date: 07.04.2008
 * Time: 16:00:45
 */
public abstract class AbstractSkypeManager<T extends ISkypeEntity> {
    /**
     * Users cache <skypeId, T>
     */
    private HashMap<String, T> cache;

    public AbstractSkypeManager(){
        try {
            cache = new HashMap<String, T>();
            List<T> list = getAllFromDB();
            for (T ent: list) {
                addToCache(ent);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    protected abstract T getFromDB(String skypeId) throws Exception;
    protected abstract List<T> getAllFromDB() throws Exception;

    protected void addToCache(T entity) {
        cache.put(entity.getSkypeId(), entity);
    }

    protected boolean cacheContains(String skypeId) {
        return get(skypeId)!=null;
    }

    public T get(String skypeId) {
        T entity = cache.get(skypeId);
        //TODO: maybe check DB if not found in cache?
        return entity;
    }
}
