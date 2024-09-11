package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.ui.boardview.BoardView;

public class Sudoku extends Puzzle {
    private SudokuView boardView;

    /** Sudoku Constructor */
    public Sudoku() {
        super();

        this.name = "Sudoku";

        this.importer = new SudokuImporter(this);
        this.exporter = new SudokuExporter(this);

        this.factory = new SudokuCellFactory();
    }

    public BoardView getBoardView() {
        return boardView;
    }

    /** Initializes the game board */
    @Override
    public void initializeView() {
        boardView = new SudokuView((SudokuBoard) currentBoard);
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

    @Override
    /**
     * Determines if the given dimensions are valid for Sudoku
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Sudoku, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        // The number of rows and columns must be greater than 1
        if (rows <= 1 || columns <= 1) {
            return false;
        }

        // The number of rows and columns must be equal
        if (rows != columns) {
            return false;
        }

        // For Sudoku, the number of rows and columns must be a perfect square
        // Note: we don't need to check the columns since by this point, we have verified that the
        // number of rows
        // equals the number of columns
        double sqrtRows = Math.sqrt(rows);
        if (sqrtRows - Math.floor(sqrtRows) != 0) {
            return false;
        }

        return true;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        SudokuBoard sudokuBoard = (SudokuBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(sudokuBoard) == null) {
                return false;
            }
        }

        for (PuzzleElement puzzleElement : sudokuBoard.getPuzzleElements()) {
            SudokuCell cell = (SudokuCell) puzzleElement;
            if (cell.getData() == 0) {
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
}
