package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.awt.*;
import java.util.List;

public class LastCampingSpotBasicRule extends BasicRule {

    public LastCampingSpotBasicRule() {
        super("Last Camping Spot",
                "If an unlinked tree is adjacent to only one blank cell and not adjacent to any unlinked tents, the blank cell must be a tent.",
                "edu/rpi/legup/images/treetent/oneTentPosition.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        if (puzzleElement instanceof TreeTentLine) {
            return "Line is not valid for this rule.";
        }
        TreeTentBoard initialBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        TreeTentCell initCell = (TreeTentCell) initialBoard.getPuzzleElement(puzzleElement);
        TreeTentBoard finalBoard = (TreeTentBoard) transition.getBoard();
        TreeTentCell finalCell = (TreeTentCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == TreeTentType.UNKNOWN && finalCell.getType() == TreeTentType.TENT)) {
            return "This cell must be a tent.";
        }

        if (isForced(initialBoard, initCell)) {
            return null;
        } else {
            return "This cell is not forced to be tent.";
        }
    }

    private boolean isForced(TreeTentBoard board, TreeTentCell cell) {
        List<TreeTentCell> adjTents = board.getAdjacent(cell, TreeTentType.TREE);
        for (TreeTentCell c : adjTents) {
            Point loc = c.getLocation();
            for (TreeTentLine line : board.getLines()) {
                if (line.getC1().getLocation().equals(loc) || line.getC2().getLocation().equals(loc)) {
                    return false;
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
        TreeTentBoard treeTentBoard = (TreeTentBoard) node.getBoard().copy();
        for (PuzzleElement element : treeTentBoard.getPuzzleElements()) {
            TreeTentCell cell = (TreeTentCell) element;
            if (cell.getType() == TreeTentType.UNKNOWN && isForced(treeTentBoard, cell)) {
                cell.setData(TreeTentType.TENT.value);
                treeTentBoard.addModifiedData(cell);
            }
        }
        if (treeTentBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return treeTentBoard;
        }
    }
}
