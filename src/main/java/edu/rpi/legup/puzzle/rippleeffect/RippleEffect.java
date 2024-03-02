package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.RegisterPuzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpView;

@RegisterPuzzle
public class RippleEffect extends Puzzle {

    public RippleEffect() {
        super();
        this.name = "RippleEffect";

        this.importer = new RippleEffectImporter(this);
        // Uncomment the following line if you have a RippleEffectExporter
        // this.exporter = new RippleEffectExporter(this);
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView() {
        boardView = new RippleEffectView((RippleEffectBoard) currentBoard);
        addBoardListener(boardView);
    }

    /**
     * Generates a random puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty) {
        // Implement if needed
        return null;
    }

    @Override
    /**
     * Determines if the given dimensions are valid for RippleEffect
     *
     * @param rows      the number of rows
     * @param columns   the number of columns
     * @return true if the given dimensions are valid for RippleEffect, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return rows > 0 && columns > 0;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board) {
        // Implement if needed
        return true;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {
        // Implement if needed
    }
}