package model.observer;

import java.util.function.Consumer;

public interface IBoardSubject
{
    /**
     * Adds a board listener
     *
     * @param listener listener to add
     */
    void addBoardListener(IBoardListener listener);

    /**
     * Removes a board listener
     *
     * @param listener listener to remove
     */
    void removeBoardListener(IBoardListener listener);

    /**
     * Notifies listeners
     *
     * @param algorithm algorithm to notify the listeners with
     */
    void notifyBoardListeners(Consumer<? super IBoardListener> algorithm);
}
