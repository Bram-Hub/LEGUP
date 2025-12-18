package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Skyscrapers extends Puzzle {
    private static final Logger LOGGER = LogManager.getLogger(Skyscrapers.class.getName());

    public Skyscrapers() {
        super();

        this.name = "Skyscrapers";

        this.importer = new SkyscrapersImporter(this);
        this.exporter = new SkyscrapersExporter(this);

        this.factory = new SkyscrapersCellFactory();
    }

    /** Initializes the game board. Called by the invoker of the class */
    @Override
    public void initializeView() {
        boardView = new SkyscrapersView((SkyscrapersBoard) currentBoard);
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
     * Determines if the given dimensions are valid for Skyscrapers
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for Skyscrapers, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return rows >= 3 && rows == columns;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        SkyscrapersBoard SkyscraperBoard = (SkyscrapersBoard) board;

        for (ContradictionRule rule : contradictionRules) {
            if (rule.checkContradiction(SkyscraperBoard) == null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(rule.getRuleName());
                }
                return false;
            }
        }
        for (PuzzleElement data : SkyscraperBoard.getPuzzleElements()) {
            SkyscrapersCell cell = (SkyscrapersCell) data;
            if (cell.getType() == SkyscrapersType.UNKNOWN) {
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
