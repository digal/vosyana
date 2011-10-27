package ru.dirty.skypebot.util;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: Sep 4, 2009
 * Time: 1:36:20 AM
 */
public class FIFOList<T> extends LinkedList<T> implements FIFO<T>{
    public T remove() {
        return remove(0);
    }
}
