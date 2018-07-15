package model.observer;

import java.util.function.Consumer;

public interface ITreeSubject
{
    /**
     * Adds a board listener
     *
     * @param listener listener to add
     */
    void addTreeListener(ITreeListener listener);

    /**
     * Removes a tree listener
     *
     * @param listener listener to remove
     */
    void removeTreeListener(ITreeListener listener);

    /**
     * Notifies listeners
     *
     * @param algorithm algorithm to notify the listeners with
     */
    void notifyTreeListeners(Consumer<? super ITreeListener> algorithm);
}
