package utility;

import app.GameBoardFacade;

import java.util.ArrayList;

public class History
{
    private ArrayList<Command> history;
    private int curIndex;

    /**
     * History Constructor - this holds information about changes to the board
     * and Tree structure for undoing and redoing operations. Though history is
     * an ArrayList, it is implemented like a stack. The curIndex points to the
     * top of the stack (where the last change was made).
     */
    public History()
    {
        history = new ArrayList<>();
        curIndex = -1;
    }

    /**
     * Pushes a change to the history list and increments the current index.
     * If the current index does not point to the top of the stack, then at least
     * 1 undo operation was called and that information will be lost by the next change
     *
     * @param command command to be pushed onto the stack
     */
    public void pushChange(Command command)
    {
        if(curIndex == history.size() - 1)
        {
            history.add(command);
        }
        else
        {
            for(int i = curIndex + 1; i < history.size(); i++)
            {
                history.remove(i);
            }
            history.add(command);
        }
        System.err.println("Pushed Change!");
        GameBoardFacade.getInstance().getLegupUI().getUndo().setEnabled(true);
        GameBoardFacade.getInstance().getLegupUI().getUndoButton().setEnabled(true);
        GameBoardFacade.getInstance().getLegupUI().getRedo().setEnabled(false);
        GameBoardFacade.getInstance().getLegupUI().getRedoButton().setEnabled(false);
        curIndex++;
    }

    public void undo()
    {
        if(curIndex > -1)
        {
            history.get(curIndex).undo();
            curIndex--;
            GameBoardFacade.getInstance().getLegupUI().getRedo().setEnabled(true);
            GameBoardFacade.getInstance().getLegupUI().getRedoButton().setEnabled(true);
        }
        else
        {
            GameBoardFacade.getInstance().getLegupUI().getRedo().setEnabled(false);
            GameBoardFacade.getInstance().getLegupUI().getRedoButton().setEnabled(false);
        }
    }

    public void redo()
    {
        if(curIndex + 1 < history.size())
        {
            history.get(++curIndex).redo();
            if(curIndex == history.size() - 1)
            {
                GameBoardFacade.getInstance().getLegupUI().getRedo().setEnabled(false);
                GameBoardFacade.getInstance().getLegupUI().getRedoButton().setEnabled(false);
            }
        }
    }

    public void clear()
    {
        history.clear();
    }

}
