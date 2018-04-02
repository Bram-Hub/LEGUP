package utility;

import model.gameboard.ElementData;

public interface IBoardListener
{
    /**
     * Called when a change to the board's data has occurred
     *
     * @param data data of the change to the board
     */
    void notifyChange(ElementData data);
}
