package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The History class manages a stack of commands for undo and redo operations on the board and tree
 * structure. It maintains a list of commands and a current index to track the position in the
 * history stack.
 */
public class History {
    private static final Logger LOGGER = LogManager.getLogger(History.class.getName());

    private final Object lock = new Object();
    private List<ICommand> history;
    private int curIndex;

    /**
     * Constructs a History object to keep track of changes and allow undo and redo operations. The
     * history is implemented as a stack, with curIndex pointing to the top of the stack.
     */
    public History() {
        history = new ArrayList<>();
        curIndex = -1;
    }

    /**
     * Pushes a change to the history list and increments the current index. If the current index
     * does not point to the top of the stack, then at least 1 undo operation was called and that
     * information will be lost by the next change
     *
     * @param command command to be pushed onto the stack
     */
    public void pushChange(ICommand command) {
        synchronized (lock) {
            if (curIndex < history.size() - 1) {
                for (int i = history.size() - 1; i > curIndex; i--) {
                    history.remove(i);
                }
            }
            history.add(command);
            curIndex++;
            LOGGER.info("Pushed " + command.getClass().getSimpleName() + " to stack.");
            GameBoardFacade.getInstance().notifyHistoryListeners(l -> l.onPushChange(command));
        }
    }

    /**
     * Undoes the last action by calling the undo method of the command at the current index.
     * Updates the current index and notifies listeners.
     */
    public void undo() {
        synchronized (lock) {
            if (curIndex > -1) {
                ICommand command = history.get(curIndex--);
                command.undo();
                LOGGER.info("Undoed " + command.getClass().getSimpleName());

                GameBoardFacade.getInstance()
                        .notifyHistoryListeners(
                                l -> l.onUndo(curIndex < 0, curIndex == history.size() - 1));
            }
        }
    }

    /**
     * Redoes the next action by calling the redo method of the command at the current index.
     * Updates the current index and notifies listeners.
     */
    public void redo() {
        synchronized (lock) {
            if (curIndex < history.size() - 1) {
                ICommand command = history.get(++curIndex);
                command.redo();
                LOGGER.info("Redoed " + command.getClass().getSimpleName());
                GameBoardFacade.getInstance()
                        .notifyHistoryListeners(
                                l -> l.onRedo(curIndex < 0, curIndex == history.size() - 1));
            }
        }
    }

    /** Clears all actions from the history stack and resets the current index */
    public void clear() {
        history.clear();
        curIndex = -1;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("History Cleared");
        }
        GameBoardFacade.getInstance().notifyHistoryListeners(IHistoryListener::onClearHistory);
    }

    /**
     * Gets the current index that points to the action at the top of stack
     *
     * @return index of the action on top of the stack
     */
    public int getIndex() {
        return curIndex;
    }

    /**
     * Gets the amount of actions that have been pushed onto the stack
     *
     * @return size of the history stack
     */
    public int size() {
        return history.size();
    }
}
