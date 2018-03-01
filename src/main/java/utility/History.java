package utility;

import java.util.ArrayList;

public class History
{
    private ArrayList<Action> history;
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
     * @param action action to be pushed onto the stack
     */
    public void pushChange(Action action)
    {
        if(history.size() == curIndex - 1)
        {
            history.add(action);
        }
        else
        {
            for(int i = curIndex + 1; i < history.size(); i++)
            {
                history.remove(i);
            }
            history.add(action);
        }
        curIndex++;
    }

    public void undo()
    {
        if(curIndex > -1)
        {
            history.get(curIndex).undo();
            curIndex--;
        }
    }

    public void redo()
    {
        if(curIndex < history.size())
        {
            history.get(curIndex).redo();
            curIndex++;
        }
    }
}
