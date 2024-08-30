package edu.rpi.legup.model.observer;

import java.util.function.Consumer;

/**
 * ITreeSubject defines methods for managing and notifying listeners about changes to the tree model.
 * Implementations of this interface handle adding, removing, and notifying listeners.
 */
public interface ITreeSubject {
    /**
     * Adds a tree listener.
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
     * Notifies all the tree listeners using the specified algorithm.
     *
     * @param algorithm algorithm used to notify the listeners
     */
    void notifyTreeListeners(Consumer<? super ITreeListener> algorithm);
}
