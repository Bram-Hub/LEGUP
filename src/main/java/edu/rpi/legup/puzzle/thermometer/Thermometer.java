package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Thermometer extends Puzzle {
    public Thermometer() {
        super();

        this.name = "Thermometer";

        this.importer = new ThermometerImporter(this);
        this.exporter = new ThermometerExporter(this);
    }

    @Override
    public void initializeView() {
        boardView = new ThermometerView((ThermometerBoard) currentBoard);
        boardView.setBoard(currentBoard);
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
