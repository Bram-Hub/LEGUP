package utility;

import model.gameboard.ElementData;

public class EditDataAction implements Action
{
    private ElementData data;
    private ElementData dataCopy;

    public EditDataAction(ElementData data)
    {
        this.data = data;
        this.dataCopy = data.copy();
    }

    /**
     * Undoes an action on the board
     */
    @Override
    public void undo()
    {

    }

    /**
     * Redoes an action on the board
     */
    @Override
    public void redo()
    {

    }
}
