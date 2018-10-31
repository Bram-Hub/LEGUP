package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class BulbsInPathContradictionRule extends ContradictionRule {

    public BulbsInPathContradictionRule() {
        super("Bulbs In Path",
                "A bulb cannot be placed in another bulb's path.",
                "edu/rpi/legup/images/lightup/contradictions/BulbsInPath.png");
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
        if (cell.getType() != LightUpCellType.BULB) {
            return "Does not contain a contradiction at this index";
        }

        Point location = cell.getLocation();
        for (int i = location.x + 1; i < lightUpBoard.getWidth(); i++) {
            LightUpCell c = lightUpBoard.getCell(i, location.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.BULB) {
                return null;
            }
        }
        for (int i = location.x - 1; i >= 0; i--) {
            LightUpCell c = lightUpBoard.getCell(i, location.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.BULB) {
                return null;
            }
        }
        for (int i = location.y + 1; i < lightUpBoard.getHeight(); i++) {
            LightUpCell c = lightUpBoard.getCell(location.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.BULB) {
                return null;
            }
        }
        for (int i = location.y - 1; i >= 0; i--) {
            LightUpCell c = lightUpBoard.getCell(location.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.BULB) {
                return null;
            }
        }
        return "Does not contain a contradiction at this index";
    }
}
