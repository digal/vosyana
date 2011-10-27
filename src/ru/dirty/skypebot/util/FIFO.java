package ru.dirty.skypebot.util;

/**
 * Created by IntelliJ IDEA.
 * Author: Yuri Buyanov
 * Date: Sep 4, 2009
 * Time: 1:35:53 AM
 */
public interface FIFO<T> {

    /** Add an object to the end of the FIFO queue */
    boolean add(T o);

    /** Remove an object from the front of the FIFO queue */
    T remove();

    /** Return the number of elements in the FIFO queue */
    int size();
}
