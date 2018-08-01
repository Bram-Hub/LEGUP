package edu.rpi.legup.utility;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public interface IBoardListener
{
    /**
     * Called when a change to the board's puzzleElement has occurred
     *
     * @param data puzzleElement of the change to the board
     */
    void notifyChange(PuzzleElement data);
}
