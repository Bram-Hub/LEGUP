package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class Nurikabe extends Puzzle {
    public Nurikabe() {
        super();

        this.name = "Nurikabe";

        this.importer = new NurikabeImporter(this);
        this.exporter = new NurikabeExporter(this);

        this.factory = new NurikabeCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        boardView = new NurikabeView((NurikabeBoard) currentBoard);
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
     * Determines if the given dimensions are valid for Nurikabe
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Nurikabe, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 2 && columns >= 2;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(nurikabeBoard) == null) {
                return false;
            }
        }

        Goal goal = this.getGoal();
        if (!isBoardFilled(nurikabeBoard)) {return false;}
        return switch (goal.getType()) {
            case PROVE_CELL_MUST_BE -> // Every goal cell is forced
                    checkGoalCells(nurikabeBoard);
            case PROVE_CELL_MIGHT_NOT_BE -> // Goal cells are different from listed
                    checkGoalCells(nurikabeBoard);
            // The goal cells are not unknown
            case PROVE_SINGLE_CELL_VALUE -> goalCellsAreKnown(nurikabeBoard);
            case PROVE_MULTIPLE_CELL_VALUE -> goalCellsAreKnown(nurikabeBoard);
            default -> true;
        };
    }

    /**
     * Determines if the current board has no unknown cells
     *
     * @param nurikabeBoard nurikabeBoard to check for empty cells
     * @return true if board has no empty cells, false otherwise
     */
    private boolean isBoardFilled(NurikabeBoard nurikabeBoard)
    {
        for (PuzzleElement data : nurikabeBoard.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) data;
            if (cell.getType() == NurikabeType.UNKNOWN) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the board's goal cells are unknown
     *
     * @param board NurikabeBoard to check cells on
     * @return true if all goal cell locations are filled, false otherwise
     */
    private boolean goalCellsAreKnown(NurikabeBoard board) {
        for (GridCell goalCell : this.getGoal().getCells())
        {
            NurikabeCell boardCell = (NurikabeCell) board.getCell(goalCell.getLocation());
            if (boardCell.getType() == NurikabeType.UNKNOWN) {return false;}
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
