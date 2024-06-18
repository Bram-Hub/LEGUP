package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import java.util.Set;

public class LastCellForNumberDirectRule extends DirectRule {
    public LastCellForNumberDirectRule() {
        super(
                "SUDO-BASC-0002",
                "Last Cell for Number",
                "This is the only cell open in its group for some number.",
                "edu/rpi/legup/images/sudoku/forcedByElimination.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParents().get(0).getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        SudokuCell cell = (SudokuCell) finalBoard.getPuzzleElement(puzzleElement);

        // Check if empty cell placed
        if (cell.getData() == 0) {
            return super.getInvalidUseOfRuleMessage() + ": Cell is not forced at this index";
        }

        // Get defaults
        Set<SudokuCell> region = initialBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = initialBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = initialBoard.getCol(cell.getLocation().x);

        // Check if new cell conflicts group
        for (SudokuCell c : region) {
            if (c.getData() == cell.getData()) {
                return super.getInvalidUseOfRuleMessage() + ": Cell is not forced at this index";
            }
        }
        for (SudokuCell c : row) {
            if (c.getData() == cell.getData()) {
                return super.getInvalidUseOfRuleMessage() + ": Cell is not forced at this index";
            }
        }
        for (SudokuCell c : col) {
            if (c.getData() == cell.getData()) {
                return super.getInvalidUseOfRuleMessage() + ": Cell is not forced at this index";
            }
        }

        // <REGION TEST>//
        // Loop to see if the number is constrained to the cell
        boolean restrained = true;
        for (SudokuCell c : region) {
            // Test if its not a valid testing cell
            if (c.getData() != 0) {
                continue;
            }
            if (c.getLocation().y == cell.getLocation().y
                    && c.getLocation().x == cell.getLocation().x) {
                continue;
            }
            // Check if cell is eligible to hold number
            Set<SudokuCell> crow = initialBoard.getRow(c.getLocation().y);
            Set<SudokuCell> ccol = initialBoard.getCol(c.getLocation().x);
            boolean contains = false;
            for (SudokuCell rc : crow) {
                if (rc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            for (SudokuCell cc : ccol) {
                if (cc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            // Stop if another cell can hold number
            if (!contains) {
                restrained = false;
                break;
            }
        }
        // Output if success
        if (restrained) {
            return null;
        }

        // <ROW TEST>//
        // Loop to see if the number is constrained to the cell
        restrained = true;
        for (SudokuCell c : row) {
            // Test if its not a valid testing cell
            if (c.getData() != 0) {
                continue;
            }
            if (c.getLocation().y == cell.getLocation().y
                    && c.getLocation().x == cell.getLocation().x) {
                continue;
            }
            // Check if cell is eligible to hold number
            Set<SudokuCell> cregion = initialBoard.getRegion(c.getGroupIndex());
            Set<SudokuCell> ccol = initialBoard.getCol(c.getLocation().x);
            boolean contains = false;
            for (SudokuCell rc : cregion) {
                if (rc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            for (SudokuCell cc : ccol) {
                if (cc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            // Stop if another cell can hold number
            if (!contains) {
                restrained = false;
                break;
            }
        }
        // Output if success
        if (restrained) {
            return null;
        }

        // <ROW TEST>//
        // Loop to see if the number is constrained to the cell
        restrained = true;
        for (SudokuCell c : col) {
            // Test if its not a valid testing cell
            if (c.getData() != 0) {
                continue;
            }
            if (c.getLocation().y == cell.getLocation().y
                    && c.getLocation().x == cell.getLocation().x) {
                continue;
            }
            // Check if cell is eligible to hold number
            Set<SudokuCell> cregion = initialBoard.getRegion(c.getGroupIndex());
            Set<SudokuCell> crow = initialBoard.getRow(c.getLocation().y);
            boolean contains = false;
            for (SudokuCell rc : cregion) {
                if (rc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            for (SudokuCell cc : crow) {
                if (cc.getData() == cell.getData()) {
                    contains = true;
                }
            }
            // Stop if another cell can hold number
            if (!contains) {
                restrained = false;
                break;
            }
        }
        // Output if success
        if (restrained) {
            return null;
        }

        // Output fail
        return super.getInvalidUseOfRuleMessage() + ": Cell is not forced at this index";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
