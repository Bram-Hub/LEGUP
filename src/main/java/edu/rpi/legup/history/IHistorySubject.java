package edu.rpi.legup.history;

import java.util.function.Consumer;

/**
 * The IHistorySubject interface defines methods for managing and notifying listeners about changes
 * in the command history. Implementations of this interface can add, remove, and notify history
 * listeners.
 */
public interface IHistorySubject {
    /**
     * Adds a history listener
     *
     * @param listener listener to add
     */
    void addHistoryListener(IHistoryListener listener);

    /**
     * Removes a history listener, so it no longer receives updates about changes in the command
     * history.
     *
     * @param listener listener to remove
     */
    void removeHistoryListener(IHistoryListener listener);

    /**
     * Notifies listeners
     *
     * @param algorithm a Consumer function that takes an IHistoryListener and performs some action
     *     with it
     */
    void notifyHistoryListeners(Consumer<? super IHistoryListener> algorithm);
}
