package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class SurroundPairDirectRule extends DirectRule {

    public SurroundPairDirectRule() {
        super(
                "BINA-BASC-0001",
                "Surround Pair",
                "If two adjacent tiles have the same value, surround the tiles with the other value.",
                "edu/rpi/legup/images/binary/rules/SurroundPairDirectRule.png");
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        // System.out.println("ORIG" + binaryCell.getData());
        // System.out.println("AFTER" + Math.abs(binaryCell.getData() - 1));
        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        PuzzleElement newP = binaryCell;

        System.out.println(contraRule.checkContradictionAt(modified, newP));

        if (contraRule.checkContradictionAt(modified, newP) == null) {
            return null;
        }
        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        return "Grouping of Three Ones or Zeros not found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
