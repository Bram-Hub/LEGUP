package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Thermometer extends Puzzle {
    public Thermometer() {
        super();
    }

    @Override
    public void initializeView() {
        boardView = new ThermometerView((ThermometerBoard) currentBoard);
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
