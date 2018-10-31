package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.Set;

public class LastCellForNumberBasicRule extends BasicRule {
    public LastCellForNumberBasicRule() {
        super("Last Cell for Number",
                "This is the only cell open in its group for some number.",
                "edu/rpi/legup/images/sudoku/forcedByElimination.png");
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
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParents().get(0).getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        SudokuCell cell = (SudokuCell) finalBoard.getPuzzleElement(puzzleElement);
        if (cell.getData() == 0) {
            return "cell is not forced at this index";
        }

        int size = initialBoard.getSize();

        Set<SudokuCell> region = initialBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = initialBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = initialBoard.getCol(cell.getLocation().x);

        boolean contains = false;
        if (region.size() == size - 1) {
            for (SudokuCell c : region) {
                if (cell.getData() == c.getData()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return null;
            }
        }
        if (row.size() == size - 1) {
            contains = false;
            for (SudokuCell c : row) {
                if (cell.getData() == c.getData()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return null;
            }
        }
        if (col.size() == size - 1) {
            contains = false;
            for (SudokuCell c : col) {
                if (cell.getData() == c.getData()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                return null;
            }
        }
        return "cell is not forced at this index";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
