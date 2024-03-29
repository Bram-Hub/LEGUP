package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.ArrayList;
import java.util.Set;

public class SurroundPairDirectRule extends DirectRule {

    public SurroundPairDirectRule() {
        super("BINA-BASC-0001",
                "Surround Pair",
                "If two adjacent tiles have the same value, surround the tiles with the other value.",
                "edu/rpi/legup/images/binary/rules/SurroundPairDirectRule.png");
    }
    
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard destBoard = (BinaryBoard) transition.getBoard();
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();

        Set<PuzzleElement> changedCells = destBoard.getModifiedData();
//        for (PuzzleElement p : changedCells) {
//            BinaryCell c = (BinaryCell) destBoard.getPuzzleElement(p);
//            if (c.getData() == 0)
//                c.setData(1);
//            else if (c.getData() == 1)
//                c.setData(0);
//            else if (c.getData() == 2)
//                return "Only ONE or ZERO cells are allowed for this rule!";
//        }
        if (contraRule.checkContradictionAt(destBoard, puzzleElement) == null) {
            return "Grouping of Three Ones or Zeros found";
        }

        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }

}