package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class History {
  private static final Logger LOGGER = LogManager.getLogger(History.class.getName());

  private final Object lock = new Object();
  private List<ICommand> history;
  private int curIndex;

  /**
   * History Constructor this holds information about changes to the board and Tree structure for
   * undoing and redoing operations. Though history is an List, it is implemented like a stack. The
   * curIndex points to the top of the stack (where the last change was made).
   */
  public History() {
    history = new ArrayList<>();
    curIndex = -1;
  }

  /**
   * Pushes a change to the history list and increments the current index. If the current index does
   * not point to the top of the stack, then at least 1 undo operation was called and that
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

  /** Undoes an action */
  public void undo() {
    synchronized (lock) {
      if (curIndex > -1) {
        ICommand command = history.get(curIndex--);
        command.undo();
        LOGGER.info("Undoed " + command.getClass().getSimpleName());
        GameBoardFacade.getInstance()
            .notifyHistoryListeners(l -> l.onUndo(curIndex < 0, curIndex == history.size() - 1));
      }
    }
  }

  /** Redoes an action */
  public void redo() {
    synchronized (lock) {
      if (curIndex < history.size() - 1) {
        ICommand command = history.get(++curIndex);
        command.redo();
        LOGGER.info("Redoed " + command.getClass().getSimpleName());
        GameBoardFacade.getInstance()
            .notifyHistoryListeners(l -> l.onRedo(curIndex < 0, curIndex == history.size() - 1));
      }
    }
  }

  /** Clears all actions from the history stack */
  public void clear() {
    history.clear();
    curIndex = -1;
    LOGGER.debug("History Cleared");
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
