package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.RegisterPuzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;


/**
 * 1) Number is duplicated in a row or a column, the space between the duplicated numbers must be equal to or larger than the value of the number.
 * 2) Each Room contains consecutive numbers starting from 1.
 * 3) If a number is duplicated in a row or a column, the space between the duplicated numbers must be equal to or larger than the value of the number.
 */



@RegisterPuzzle
public class RippleEffect extends Puzzle {

    public RippleEffect() {
        super();
        this.name = "RippleEffect";

        this.importer = new RippleEffectImporter(this);
        // this.exporter = new LightUpExporter(this);

        // this.factory = new LightUpCellFactory();
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView() {
        // boardView = new LightUpView((LightUpBoard) currentBoard);
        // boardView.setBoard(currentBoard);
        // addBoardListener(boardView);
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
     * @param rows      the number of rows
     * @param columns   the number of columns
     * @return true if the given dimensions are valid for Light Up, false otherwise
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
        // LightUpBoard lightUpBoard = (LightUpBoard) board;
        // lightUpBoard.fillWithLight();

        // for (ContradictionRule rule : contradictionRules) {
        //     if (rule.checkContradiction(lightUpBoard) == null) {
        //         System.out.println(rule.getRuleName());
        //         return false;
        //     }
        // }
        // for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
        //     LightUpCell cell = (LightUpCell) data;
        //     if ((cell.getType() == LightUpCellType.UNKNOWN || cell.getType() == LightUpCellType.EMPTY) && !cell.isLite()) {
        //         return false;
        //     }
        // }
        return true;
    }

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board) {

    }
}
