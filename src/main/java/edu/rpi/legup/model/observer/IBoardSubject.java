package edu.rpi.legup.model.observer;

import java.util.function.Consumer;

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
     * Notifies all of the listeners using the specified algorithm.
     *
     * @param algorithm algorithm used to notify the listeners
     */
    void notifyBoardListeners(Consumer<? super IBoardListener> algorithm);
}
