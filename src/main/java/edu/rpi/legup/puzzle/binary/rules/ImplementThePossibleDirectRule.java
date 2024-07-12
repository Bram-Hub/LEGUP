package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImplementThePossibleDirectRule extends DirectRule {

    public ImplementThePossibleDirectRule() {
        super(
                "BINA-BASC-0005",
                "Implement The Possible",
                "If three adjacent empty cells are open, prevents a trio of numbers to exist",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return "0s";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
