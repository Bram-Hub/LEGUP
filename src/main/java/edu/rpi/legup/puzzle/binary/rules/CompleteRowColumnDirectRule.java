package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

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
        ContradictionRule contraRule = new UnbalancedRowOrColumnContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();
        BinaryCell c = (BinaryCell) modified.getPuzzleElement(puzzleElement);

        // System.out.println("ORIG" + binaryCell.getData());
        // System.out.println("AFTER" + Math.abs(binaryCell.getData() - 1));
        modified.getPuzzleElement(puzzleElement).setData(binaryCell.getData());
        System.out.println(contraRule.checkContradictionAt(modified, puzzleElement));

        if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
            return null;
        }

        return "Grouping of Three Ones or Zeros not found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
