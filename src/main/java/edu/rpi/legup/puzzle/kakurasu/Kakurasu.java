package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCellFactory;
import edu.rpi.legup.puzzle.nurikabe.NurikabeExporter;
import edu.rpi.legup.puzzle.nurikabe.NurikabeImporter;

public class Kakurasu extends Puzzle {
    public Kakurasu() {
        super();
        this.name = "Kakurasu";

        this.importer = new KakurasuImporter(this);
        this.exporter = new KakurasuExporter(this);

        this.factory = new KakurasuCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new KakurasuView((KakurasuBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    /**
     * Determines if the given dimensions are valid for Kakurasu
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Kakurasu, false otherwise
     */
    @Override
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 3 && rows == columns;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        return true;
    }

    @Override
    public void onBoardChange(Board board) {
    }
}
