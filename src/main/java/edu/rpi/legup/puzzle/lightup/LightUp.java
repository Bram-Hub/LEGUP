package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.RegisterPuzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.Rule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RegisterPuzzle
public class LightUp extends Puzzle {
    private static final Logger LOGGER = LogManager.getLogger(LightUp.class.getName());

    public LightUp() {
        super();
        this.name = "LightUp";

        this.importer = new LightUpImporter(this);
        this.exporter = new LightUpExporter(this);

        this.factory = new LightUpCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        boardView = new LightUpView((LightUpBoard) currentBoard);
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
     * Determines if the given dimensions are valid for Light Up
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Light Up, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 0 && columns >= 0;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        LightUpBoard lightUpBoard = (LightUpBoard) board;
        lightUpBoard.fillWithLight();

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(lightUpBoard) == null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(rule.getRuleName());
                }
                return false;
            }
        }
        for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) data;
            if ((cell.getType() == LightUpCellType.UNKNOWN
                            || cell.getType() == LightUpCellType.EMPTY)
                    && !cell.isLite()) {
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
