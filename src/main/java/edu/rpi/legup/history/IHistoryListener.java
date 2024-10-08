package edu.rpi.legup.history;

/**
 * The IHistoryListener interface defines methods for listening to changes in the history of
 * commands. Implementations of this interface can respond to events related to command history such
 * as pushing, undoing, redoing commands, and clearing the history.
 */
public interface IHistoryListener {
    /**
     * Called when a action is pushed onto the edu.rpi.legup.history stack
     *
     * @param command action to push onto the stack
     */
    void onPushChange(ICommand command);

    /**
     * Called when an action is undone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    void onUndo(boolean isBottom, boolean isTop);

    /**
     * Called when an action is redone
     *
     * @param isBottom true if there are no more actions to undo, false otherwise
     * @param isTop true if there are no more changes to redo, false otherwise
     */
    void onRedo(boolean isBottom, boolean isTop);

    /** Called when the history stack is cleared. */
    void onClearHistory();
}
