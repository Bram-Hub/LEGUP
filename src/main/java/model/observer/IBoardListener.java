package model.observer;

import model.gameboard.Board;
import model.gameboard.Element;

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
