package edu.rpi.legup.model.observer;

import java.util.function.Consumer;

public interface ITreeSubject {
    /**
     * Adds a board listener.
     *
     * @param listener listener to add
     */
    void addTreeListener(ITreeListener listener);

    /**
     * Removes a tree listener.
     *
     * @param listener listener to remove
     */
    void removeTreeListener(ITreeListener listener);

    /**
     * Notifies all of the listeners using the specified algorithm.
     *
     * @param algorithm algorithm used to notify the listeners
     */
    void notifyTreeListeners(Consumer<? super ITreeListener> algorithm);
}
