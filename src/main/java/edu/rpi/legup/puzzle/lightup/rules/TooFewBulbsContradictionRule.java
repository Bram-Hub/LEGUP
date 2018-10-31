package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class TooFewBulbsContradictionRule extends ContradictionRule {

    public TooFewBulbsContradictionRule() {
        super("Too Few Bulbs",
                "There cannot be less bulbs around a block than its number states.",
                "edu/rpi/legup/images/lightup/contradictions/TooFewBulbs.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        LightUpBoard lightUpBoard = (LightUpBoard) board;
        LightUpCell cell = (LightUpCell) lightUpBoard.getPuzzleElement(puzzleElement);
        if (cell.getType() != LightUpCellType.NUMBER) {
            return "Does not contain a contradiction";
        }

        Point location = cell.getLocation();

        int bulbs = lightUpBoard.getNumAdj(cell, LightUpCellType.BULB);
        int placeable = lightUpBoard.getNumPlacble(cell);

        if (bulbs + placeable < cell.getData()) {
            return null;
        }
        return "Number does not contain a contradiction";
    }
}
