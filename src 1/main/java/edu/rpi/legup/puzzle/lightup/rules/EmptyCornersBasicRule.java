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
import java.util.ArrayList;
import java.util.List;

public class EmptyCornersBasicRule extends BasicRule {

    public EmptyCornersBasicRule() {
        super("Empty Corners",
                "Cells on the corners of a number must be empty if placing bulbs would prevent the number from being satisfied.",
                "edu/rpi/legup/images/lightup/rules/EmptyCorners.png");
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
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpCell cell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);

        if (!(cell.getType() == LightUpCellType.UNKNOWN && finalCell.getType() == LightUpCellType.EMPTY)) {
            return "This cell must be an empty cell.";
        }

        Point loc = finalCell.getLocation();
        List<LightUpCell> numberedCells = new ArrayList<>();
        LightUpCell upperRight = finalBoard.getCell(loc.x + 1, loc.y - 1);
        if (upperRight != null && upperRight.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(upperRight);
        }
        LightUpCell upperLeft = finalBoard.getCell(loc.x - 1, loc.y - 1);
        if (upperLeft != null && upperLeft.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(upperLeft);
        }
        LightUpCell lowerRight = finalBoard.getCell(loc.x + 1, loc.y + 1);
        if (lowerRight != null && lowerRight.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(lowerRight);
        }
        LightUpCell lowerLeft = finalBoard.getCell(loc.x - 1, loc.y + 1);
        if (lowerLeft != null && lowerLeft.getType() == LightUpCellType.NUMBER) {
            numberedCells.add(lowerLeft);
        }
        if (numberedCells.isEmpty()) {
            return "This cell must diagonal to a numbered cell.";
        }

        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
        LightUpBoard bulbCaseBoard = finalBoard.copy();
        LightUpCell bulbCaseCell = (LightUpCell) bulbCaseBoard.getPuzzleElement(puzzleElement);
        bulbCaseCell.setData(LightUpCellType.BULB.value);
        bulbCaseBoard.fillWithLight();

        boolean createsContra = false;
        for (LightUpCell c : numberedCells) {
            createsContra |= tooFew.checkContradictionAt(bulbCaseBoard, c) == null;
        }
        if (createsContra) {
            return null;
        } else {
            return "This cell is not forced to be empty.";
        }
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard lightUpBoard = (LightUpBoard) node.getBoard().copy();
        LightUpBoard lightUpBoardCopy = (LightUpBoard) node.getBoard().copy();
        TreeTransition transition = new TreeTransition(node, lightUpBoardCopy);
        for (PuzzleElement element : lightUpBoardCopy.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) element;
            int temp = cell.getData();
            cell.setData(LightUpCellType.EMPTY.value);
            if (checkRuleRawAt(transition, cell) == null) {
                LightUpCell modCell = (LightUpCell) lightUpBoard.getPuzzleElement(cell);
                modCell.setData(LightUpCellType.EMPTY.value);
                lightUpBoard.addModifiedData(modCell);
            } else {
                cell.setData(temp);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
