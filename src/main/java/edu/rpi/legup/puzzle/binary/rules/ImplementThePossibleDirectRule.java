package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.ArrayList;

public class ImplementThePossibleDirectRule extends DirectRule {

    public ImplementThePossibleDirectRule() {
        super(
                "BINA-BASC-0005",
                "Implement The Possible",
                "If three adjacent empty cells are open, prevents a trio of numbers to exist",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    /**
     * Checks if placing a digit in the given cell would create an invalid
     * configuration with three consecutive identical digits.
     *
     * @param origBoard  The original board state.
     * @param binaryCell The cell to check.
     * @return True if placing the digit would prevent an invalid trio, false otherwise.
     */
    private boolean wouldPreventInvalidTrio(BinaryBoard origBoard, BinaryCell binaryCell) {
        ArrayList<BinaryCell> row = origBoard.listRowCells(binaryCell.getLocation().y);
        ArrayList<BinaryCell> col = origBoard.listColCells(binaryCell.getLocation().x);

        return wouldCreateInvalidTrio(row, binaryCell.getLocation().x, binaryCell.getData()) ||
               wouldCreateInvalidTrio(col, binaryCell.getLocation().y, binaryCell.getData());
    }

    /**
     * Checks if placing a digit at the specified position in a row or column would
     * create an invalid trio of consecutive identical digits.
     *
     * @param line     The list of cells in the row or column.
     * @param pos      The position where the digit is to be placed.
     * @param digit    The digit to be placed.
     * @return True if placing the digit would create an invalid trio, false otherwise.
     */
    private boolean wouldCreateInvalidTrio(ArrayList<BinaryCell> line, int pos, int digit) {
        if (pos > 1 && line.get(pos - 1).getData() == digit && line.get(pos - 2).getData() == digit) {
            return true;
        }
        if (pos < line.size() - 2 && line.get(pos + 1).getData() == digit && line.get(pos + 2).getData() == digit) {
            return true;
        }
        if (pos > 0 && pos < line.size() - 1 &&
            line.get(pos - 1).getData() == digit && line.get(pos + 1).getData() == digit) {
            return true;
        }
        return false;
    }

    /**
     * Checks the rule for the given tree transition and puzzle element.
     *
     * @param transition    The current tree transition.
     * @param puzzleElement The puzzle element to check.
     * @return A message if the rule is invalid, null otherwise.
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        if (wouldPreventInvalidTrio(origBoard, binaryCell)) {
            return null;
        }

        return "Placing this digit would create an invalid trio of consecutive identical digits.";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
