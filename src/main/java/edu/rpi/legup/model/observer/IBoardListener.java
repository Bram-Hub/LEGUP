package edu.rpi.legup.model.observer;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public interface IBoardListener {
    /**
     * Called when the board has changed.
     *
     * @param board board that has changed
     */
    void onBoardChanged(Board board);

    /**
     * Called when a {@link PuzzleElement}'s data on the board has changed.
     *
     * @param puzzleElement puzzleElement that has changed
     */
    void onBoardDataChanged(PuzzleElement puzzleElement);
}
