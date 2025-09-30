package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
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
        return switch (goal.getType()) {
            case PROVE_CELL_MUST_BE -> // Every goal cell is forced
                    checkGoalCells(nurikabeBoard, goal);
            case PROVE_CELL_MIGHT_NOT_BE -> {
                // If the goal cell is guaranteed to be the same, then the board
                // is technically true? Need to fact-check this, but I think this is right

                // Board is complete and goal cell is different from listed
                for (PuzzleElement data : nurikabeBoard.getPuzzleElements()) {
                    NurikabeCell cell = (NurikabeCell) data;
                    if (cell.getType() == NurikabeType.UNKNOWN) {
                        yield false;
                    }
                }

                for (GridCell goalCell : goal.getCells()){
                    GridCell boardCell = nurikabeBoard.getCell(goalCell.getLocation());
                    if (boardCell.equals(goalCell)){
                        yield false;
                    }
                }

                yield true;
            }
            case PROVE_SINGLE_CELL_VALUE -> { yield false; }
            case PROVE_MULTIPLE_CELL_VALUE -> { yield false; }
            default -> {
                for (PuzzleElement data : nurikabeBoard.getPuzzleElements()) {
                    NurikabeCell cell = (NurikabeCell) data;
                    if (cell.getType() == NurikabeType.UNKNOWN) {
                        yield false;
                    }
                }
                yield true;
            }
        };
    }

    /**
     * Returns true if all the cells listed in the Goal are forced.
     * @param goal Goal object containing cell locations and values.
     * @return True if all the cells match the ones specified in Goal, False otherwise.
     */
    private boolean checkGoalCells(NurikabeBoard board, Goal goal) {
        boolean isValid = false;
        for (GridCell goalCell : goal.getCells()) {
            GridCell boardCell = board.getCell(goalCell.getLocation());
            if (boardCell.equals(goalCell)){
                isValid = true;
            }
        }

        return isValid;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {}
}
