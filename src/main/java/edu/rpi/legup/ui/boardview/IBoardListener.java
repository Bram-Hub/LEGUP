package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.model.gameboard.Board;

public interface IBoardListener
{
    /**
     * The gameboard state element has been changed
     *
     * @param state the updated gameboard state
     */
    void boardDataChanged(Board state);
}
