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

public class CompleteRowColumnDirectRule extends DirectRule {

    public CompleteRowColumnDirectRule() {
        super("BINA-BASC-0003",
                "Complete Row Column",
                "If a row/column of length n contains n/2 of a single value, the remaining cells must contain the other value",
                "FILL IN WITH IMAGE");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}