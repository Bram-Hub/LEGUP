package edu.rpi.legup.history;

import java.util.function.Consumer;

public interface IHistorySubject {
  /**
   * Adds a history listener
   *
   * @param listener listener to add
   */
  void addHistoryListener(IHistoryListener listener);

  /**
   * Removes a history listener
   *
   * @param listener listener to remove
   */
  void removeHistoryListener(IHistoryListener listener);

  /**
   * Notifies listeners
   *
   * @param algorithm algorithm to notify the listeners with
   */
  void notifyHistoryListeners(Consumer<? super IHistoryListener> algorithm);
}
