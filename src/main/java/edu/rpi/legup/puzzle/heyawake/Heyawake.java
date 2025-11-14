package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class Heyawake extends Puzzle {

    public Heyawake() {
        super();

        this.name = "Heyawake";

        this.factory = new HeyawakeFactory();

        this.exporter = new HeyawakeExporter(this);
        this.importer = new HeyawakeImporter(this);
    }

    /** Initializes the view. Called by the invoker of the class */
    @Override
    public void initializeView() {
        boardView = new HeyawakeView((HeyawakeBoard) currentBoard);
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
     * Determines if the given dimensions are valid for HeyAwake
     *
     * @param rows the number of rows
     * @param columns the number of columns
     * @return true if the given dimensions are valid for HeyAwake, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        // This is a placeholder, this method needs to be implemented
        throw new UnsupportedOperationException();
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        return false;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {}
}
