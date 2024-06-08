package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.Binary;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;

public class CompleteRowColumnDirectRule extends DirectRule {

    public CompleteRowColumnDirectRule() {
        super(
                "BINA-BASC-0003",
                "Complete Row Column",
                "If a row/column of length n contains n/2 of a single value, the remaining cells must contain the other value",
                "edu/rpi/legup/images/binary/rules/CompleteRowColumnDirectRule.png");
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
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        modified.getPuzzleElement(puzzleElement).setData(binaryCell.getData());

        Set<BinaryCell> row = modified.getRowCells(binaryCell.getLocation().y);
        int size = row.size();
        int rowNumZeros = 0;
        int rowNumOnes = 0;

        for (BinaryCell item : row) {
            if (item.getType() == BinaryType.ZERO) {
                rowNumZeros++;
            } else if (item.getType() == BinaryType.ONE) {
                rowNumOnes++;
            }
        }

        if (rowNumZeros == size / 2 && binaryCell.getType() == BinaryType.ONE) {
            return null;
        }
        else if (rowNumOnes == size / 2 && binaryCell.getType() == BinaryType.ZERO) {
            return null;
        }

        Set<BinaryCell> col = modified.getColCells(binaryCell.getLocation().x);
        int colNumZeros = 0;
        int colNumOnes = 0;

        for (BinaryCell item : col) {
            if (item.getType() == BinaryType.ZERO) {
                colNumZeros++;
            } else if (item.getType() == BinaryType.ONE) {
                colNumOnes++;
            }
        }

        if (colNumZeros == size / 2 && binaryCell.getType() == BinaryType.ONE) {
            return null;
        }
        else if (colNumOnes == size / 2 && binaryCell.getType() == BinaryType.ZERO) {
            return null;
        }

//        if (contraRule.checkContradictionAt(modified, binaryCell) != null) {
//            return null;
//        }

        return "Row or column unbalanced";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
