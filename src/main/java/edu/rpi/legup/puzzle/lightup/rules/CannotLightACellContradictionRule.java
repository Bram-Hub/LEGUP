package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class CannotLightACellContradictionRule extends ContradictionRule {

    public CannotLightACellContradictionRule() {
        super("LTUP-CONT-0002", "Cannot Light A Cell",
                "All cells must be able to be lit.",
                "edu/rpi/legup/images/lightup/contradictions/CannotLightACell.png");
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
        /*if (cell.getType() == LightUpCellType.BLACK || cell.getType() == LightUpCellType.NUMBER || cell.isLite()) {
            return "This cell does not contain a contradiction";
        }*/
        if (cell.getType() != LightUpCellType.EMPTY || cell.isLite()) {
            return super.getNoContradictionMessage();
        }
        Point location = cell.getLocation();
        int ver_count = 0;
        int hor_count = 0;
        for (int i = location.x + 1; i < lightUpBoard.getWidth(); i++) {
            LightUpCell c = lightUpBoard.getCell(i, location.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                hor_count += 1;
            }
        }
        for (int i = location.x - 1; i >= 0; i--) {
            LightUpCell c = lightUpBoard.getCell(i, location.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                hor_count += 1;
            }
        }
        for (int i = location.y + 1; i < lightUpBoard.getHeight(); i++) {
            LightUpCell c = lightUpBoard.getCell(location.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                ver_count += 1;
            }
        }
        for (int i = location.y - 1; i >= 0; i--) {
            LightUpCell c = lightUpBoard.getCell(location.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                ver_count += 1;
            }
        }
        System.out.printf("%d, %d, %d, %d\n", location.x, location.y, hor_count, ver_count);
        if(hor_count == 0 && ver_count == 0) {
            return null;
        }
        return super.getNoContradictionMessage();
    }
}