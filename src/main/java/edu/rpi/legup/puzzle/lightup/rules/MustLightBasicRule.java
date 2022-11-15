package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class MustLightBasicRule extends BasicRule {

    public MustLightBasicRule() {
        super("LTUP-BASC-0006", "Must Light",
                "A cell must be a bulb if it is the only cell to be able to light another.",
                "edu/rpi/legup/images/lightup/rules/MustLight.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        LightUpBoard parentBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell parentCell = (LightUpCell) parentBoard.getPuzzleElement(puzzleElement);
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(parentCell.getType() == LightUpCellType.UNKNOWN && !parentCell.isLite() && finalCell.getType() == LightUpCellType.BULB)) {
            return super.getInvalidUseOfRuleMessage() + ": Modified cells must be bulbs";
        }

        finalBoard.fillWithLight();
        boolean isForced = isForcedBulb(parentBoard, parentCell.getLocation());
        finalCell.setData(LightUpCellType.BULB.value);
        finalBoard.fillWithLight();

        if (isForced) {
            return null;
        }
        else {
            return super.getInvalidUseOfRuleMessage() + ": This cell can be lit by another cell";
        }
    }

    private boolean isForcedBulb(LightUpBoard board, Point loc) {
        CannotLightACellContradictionRule cannotLite = new CannotLightACellContradictionRule();
        LightUpBoard modifiedBoard = board.copy();
        LightUpCell modifiedCell = modifiedBoard.getCell(loc.x, loc.y);
        modifiedCell.setData(LightUpCellType.EMPTY.value);
        //Check if this cell itself (the one with the bulb) has no other lighting option
        if ((modifiedCell.getType() == LightUpCellType.EMPTY || modifiedCell.getType() == LightUpCellType.UNKNOWN) &&
            !modifiedCell.isLite() && cannotLite.checkContradictionAt(modifiedBoard, modifiedCell) == null) {
            return true;
        }
        //Look right
        for (int i = loc.x + 1; i < modifiedBoard.getWidth(); i++) {
            LightUpCell c = modifiedBoard.getCell(i, loc.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            }
            else {
                if (c.getType() == LightUpCellType.EMPTY &&
                        !c.isLite() && cannotLite.checkContradictionAt(modifiedBoard, c) == null) {
                    return true;
                }
            }
        }
        //Look left
        for (int i = loc.x - 1; i >= 0; i--) {
            LightUpCell c = modifiedBoard.getCell(i, loc.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            }
            else {
                if (c.getType() == LightUpCellType.EMPTY &&
                        !c.isLite() && cannotLite.checkContradictionAt(modifiedBoard, c) == null) {
                    return true;
                }
            }
        }
        //Look down
        for (int i = loc.y + 1; i < modifiedBoard.getHeight(); i++) {
            LightUpCell c = modifiedBoard.getCell(loc.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            }
            else {
                if (c.getType() == LightUpCellType.EMPTY &&
                        !c.isLite() && cannotLite.checkContradictionAt(modifiedBoard, c) == null) {
                    return true;
                }
            }
        }
        //Look up
        for (int i = loc.y - 1; i >= 0; i--) {
            LightUpCell c = modifiedBoard.getCell(loc.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            }
            else {
                if (c.getType() == LightUpCellType.EMPTY &&
                        !c.isLite() && cannotLite.checkContradictionAt(modifiedBoard, c) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard initialBoard = (LightUpBoard) node.getBoard();
        LightUpBoard tempBoard = (LightUpBoard) node.getBoard().copy();
        LightUpBoard lightUpBoard = (LightUpBoard) node.getBoard().copy();
        for (PuzzleElement element : tempBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) element;
            if (cell.getType() == LightUpCellType.UNKNOWN && !cell.isLite()) {
                cell.setData(LightUpCellType.EMPTY.value);
                if (isForcedBulb(initialBoard, cell.getLocation())) {
                    LightUpCell modCell = (LightUpCell) lightUpBoard.getPuzzleElement(cell);
                    modCell.setData(LightUpCellType.BULB.value);
                    lightUpBoard.addModifiedData(modCell);
                }
                cell.setData(LightUpCellType.UNKNOWN.value);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        }
        else {
            return lightUpBoard;
        }
    }
}
