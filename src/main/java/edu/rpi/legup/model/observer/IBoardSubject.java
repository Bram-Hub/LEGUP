package edu.rpi.legup.model.observer;

import java.util.function.Consumer;

/**
 * IBoardSubject defines methods for managing and notifying board listeners. It allows for adding
 * and removing listeners, and for notifying them using a specified algorithm.
 */
public interface IBoardSubject {
    /**
     * Adds a board listener.
     *
     * @param listener listener to add
     */
    void addBoardListener(IBoardListener listener);

    /**
     * Removes a board listener.
     *
     * @param listener listener to remove
     */
    void removeBoardListener(IBoardListener listener);

    /**
     * Notifies all the listeners using the specified algorithm.
     *
     * @param algorithm algorithm used to notify the listeners
     */
    void notifyBoardListeners(Consumer<? super IBoardListener> algorithm);
}
