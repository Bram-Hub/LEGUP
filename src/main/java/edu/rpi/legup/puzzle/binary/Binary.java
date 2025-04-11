package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class Binary extends Puzzle {
    public Binary() {
        super();

        this.name = "Binary";

        this.importer = new BinaryImporter(this);
        this.exporter = new BinaryExporter(this);

        this.factory = new BinaryCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        boardView = new BinaryView((BinaryBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random edu.rpi.legup.puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
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

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {}

    /**
     * Determines if the given dimensions are valid for Binary
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Binary, false otherwise
     */
    @Override
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 2 && rows % 2 == 0 && rows == columns;
    }
}
