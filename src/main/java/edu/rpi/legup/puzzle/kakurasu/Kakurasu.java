package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Kakurasu extends Puzzle {
    public Kakurasu() {
        super();
    }

    @Override
    public void initializeView() {
        boardView = new KakurasuView((KakurasuBoard) currentBoard);
        addBoardListener(boardView);
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
