package utility;

import app.GameBoardFacade;

import java.util.ArrayList;

public class History
{
    private ArrayList<ICommand> history;
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
    public void pushChange(ICommand command)
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
        GameBoardFacade.getInstance().getLegupUI().getTreePanel().updateStatus("");
        setUndoUI(true);
        setRedoUI(false);
        curIndex++;
    }

    public void undo()
    {
        if(curIndex > -1)
        {
            history.get(curIndex--).undo();
            if(curIndex < 0)
            {
                setUndoUI(false);
            }
            setRedoUI(true);
        }
    }

    public void redo()
    {
        if(curIndex < history.size() - 1)
        {
            history.get(++curIndex).redo();
            if(curIndex == history.size() - 1)
            {
                setRedoUI(false);
            }
            if(curIndex >= 0)
            {
                setUndoUI(true);
            }
        }
    }

    public void clear()
    {
        history.clear();
        curIndex = -1;
        setUndoUI(false);
        setRedoUI(false);
    }

    public int getIndex()
    {
        return curIndex;
    }


    public void setUndoUI(boolean enabled)
    {
        GameBoardFacade.getInstance().getLegupUI().getUndo().setEnabled(enabled);
        GameBoardFacade.getInstance().getLegupUI().getUndoButton().setEnabled(enabled);
    }

    public void setRedoUI(boolean enabled)
    {

        GameBoardFacade.getInstance().getLegupUI().getRedo().setEnabled(enabled);
        GameBoardFacade.getInstance().getLegupUI().getRedoButton().setEnabled(enabled);
    }
}
