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
        super("Must Light",
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
            return "Modified cells must be bulbs";
        }

        finalCell.setData(LightUpCellType.EMPTY.value);
        finalBoard.fillWithLight();
        boolean isForced = isForcedBulb(finalBoard, finalCell.getLocation());
        finalCell.setData(LightUpCellType.BULB.value);
        finalBoard.fillWithLight();

        if (isForced) {
            return null;
        } else {
            return "This cell can be lite by another cell.";
        }
    }

    private boolean isForcedBulb(LightUpBoard board, Point loc) {
        CannotLightACellContradictionRule cannotLite = new CannotLightACellContradictionRule();
        LightUpCell cell = board.getCell(loc.x, loc.y);
        if ((cell.getType() == LightUpCellType.EMPTY || cell.getType() == LightUpCellType.UNKNOWN) &&
                !cell.isLite() && cannotLite.checkContradictionAt(board, cell) == null) {
            return true;
        }
        for (int i = loc.x + 1; i < board.getWidth(); i++) {
            LightUpCell c = board.getCell(i, loc.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if ((c.getType() == LightUpCellType.EMPTY || c.getType() == LightUpCellType.UNKNOWN) &&
                    !c.isLite() && cannotLite.checkContradictionAt(board, c) == null) {
                return true;
            }
        }
        for (int i = loc.x - 1; i >= 0; i--) {
            LightUpCell c = board.getCell(i, loc.y);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if ((c.getType() == LightUpCellType.EMPTY || c.getType() == LightUpCellType.UNKNOWN) &&
                    !c.isLite() && cannotLite.checkContradictionAt(board, c) == null) {
                return true;
            }
        }
        for (int i = loc.y + 1; i < board.getHeight(); i++) {
            LightUpCell c = board.getCell(loc.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if ((c.getType() == LightUpCellType.EMPTY || c.getType() == LightUpCellType.UNKNOWN) &&
                    !c.isLite() && cannotLite.checkContradictionAt(board, c) == null) {
                return true;
            }
        }
        for (int i = loc.y - 1; i >= 0; i--) {
            LightUpCell c = board.getCell(loc.x, i);
            if (c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER) {
                break;
            } else if ((c.getType() == LightUpCellType.EMPTY || c.getType() == LightUpCellType.UNKNOWN) &&
                    !c.isLite() && cannotLite.checkContradictionAt(board, c) == null) {
                return true;
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
        } else {
            return lightUpBoard;
        }
    }
}