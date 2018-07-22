package ui.boardview;

import model.gameboard.Board;

public interface IBoardListener
{
    /**
     * The gameboard state element has been changed
     *
     * @param state the updated gameboard state
     */
    void boardDataChanged(Board state);
}
