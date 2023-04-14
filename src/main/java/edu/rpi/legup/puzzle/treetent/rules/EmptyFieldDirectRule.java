package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.util.List;

public class EmptyFieldDirectRule extends DirectRule {
    public EmptyFieldDirectRule() {
        super("TREE-BASC-0001", "Empty Field",
                "Blank cells not adjacent to an unlinked tree are grass.",
                "edu/rpi/legup/images/treetent/noTreesAround.png");
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
            return super.getInvalidUseOfRuleMessage() + ": Line is not valid for this rule";
        }
        TreeTentBoard initialBoard = (TreeTentBoard) transition.getParents().get(0).getBoard();
        TreeTentCell initCell = (TreeTentCell) initialBoard.getPuzzleElement(puzzleElement);
        TreeTentBoard finalBoard = (TreeTentBoard) transition.getBoard();
        TreeTentCell finalCell = (TreeTentCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(finalCell.getType() == TreeTentType.GRASS && initCell.getType() == TreeTentType.UNKNOWN)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell must be grass";
        }

        if (isForced(finalBoard, finalCell)) {
            return null;
        }
        else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty.";
        }
    }

    /**
     * Returns a boolean value based on whether the specified cell has adjacent cells (true - no adjacent, false - has adjacent)
     *
     * @param board the TreeTent board
     * @param cell  the specified TreeTent cell
     * @return true - no adjacent, false - has adjacent
     */
    private boolean isForced(TreeTentBoard board, TreeTentCell cell) {
        List<TreeTentCell> adjCells = board.getAdjacent(cell, TreeTentType.TREE);
        return adjCells.isEmpty();
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
                cell.setData(TreeTentType.GRASS);
                treeTentBoard.addModifiedData(cell);
            }
        }
        if (treeTentBoard.getModifiedData().isEmpty()) {
            return null;
        }
        else {
            return treeTentBoard;
        }
    }
}
