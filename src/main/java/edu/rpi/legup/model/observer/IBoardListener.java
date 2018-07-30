package edu.rpi.legup.model.observer;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.Element;

public interface IBoardListener
{
    /**
     * Called when the board has changed
     *
     * @param board board that has changed
     */
    void onBoardChanged(Board board);

    /**
     * Called when the board element changed
     *
     * @param data element of the element that changed
     */
    void onBoardDataChanged(Element data);
}
