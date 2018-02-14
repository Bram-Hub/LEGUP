package ui.boardview;

import model.gameboard.Board;

public interface IBoardListener
{
    /**
     * The gameboard state data has been changed
     *
     * @param state the updated gameboard state
     */
    void boardDataChanged(Board state);
}
