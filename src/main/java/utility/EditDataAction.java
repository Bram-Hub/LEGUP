package utility;

import model.gameboard.ElementData;
import model.tree.TreeTransition;

public class EditDataAction implements Action
{
    private TreeTransition transition;
    private ElementData oldData;
    private ElementData newData;

    public EditDataAction(TreeTransition transition, ElementData oldData, ElementData newData)
    {
        this.transition = transition;
        this.oldData = oldData.copy();
        this.newData = newData.copy();
    }

    /**
     * Undoes an action on the board
     */
    @Override
    public void undo()
    {
        transition.getBoard().notifyChange(oldData);
        transition.propagateChanges(oldData);
    }

    /**
     * Redoes an action on the board
     */
    @Override
    public void redo()
    {
        transition.getBoard().notifyChange(newData);
        transition.propagateChanges(newData);
    }
}
