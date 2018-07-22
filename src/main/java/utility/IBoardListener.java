package utility;

import model.gameboard.ElementData;

public interface IBoardListener
{
    /**
     * Called when a change to the board's element has occurred
     *
     * @param data element of the change to the board
     */
    void notifyChange(ElementData data);
}
