package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Binary extends Puzzle {
    public Binary() {
        super();
    }

    @Override
    public void initializeView() {
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        return true;
    }

    @Override
    public void onBoardChange(Board board) {
    }
}