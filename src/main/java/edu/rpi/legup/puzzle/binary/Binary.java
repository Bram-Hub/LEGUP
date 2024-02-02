package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Binary extends Puzzle {
    public Binary() {
        super();

        this.name = "Binary";
        this.importer = new BinaryImporter(this);
        this.exporter = new BinaryExporter(this);

        this.factory = new BinaryCellFactory(this);
    }

    @Override
    public void initializeView() {
        boardView = new BinaryView((BinaryBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListner(boardView);
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
    

    @Overrride
    public boolean isValidDimensions(int rows, int columns){
        return rows >= 2 && rows % 2 == 0 && columns >= 2 && columns % 2 == 0;
    }

    Override
    public boolean isBoardComplete(Board board) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(binaryBoard) == null) {
                return false;
            }
        }
        for (PuzzleElement data : binaryBoard.getPuzzleElements()) {
            BinaryCell cell = (BinaryCell) data;
            if (cell.getType() == BinaryType.UNKNOWN) {
                return false;
            }
        }
        return true;
    }
}