package model.observer;

import model.gameboard.Board;
import model.gameboard.ElementData;

public interface IBoardListener
{
    /**
     * Called when the board has changed
     *
     * @param board board that has changed
     */
    void onBoardChanged(Board board);

    /**
     * Called when the board data changed
     *
     * @param data data of the element that changed
     */
    void onBoardDataChanged(ElementData data);
}
