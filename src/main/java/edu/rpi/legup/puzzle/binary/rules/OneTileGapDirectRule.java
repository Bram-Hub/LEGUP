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

public class OneTileGapDirectRule extends DirectRule {

    public OneTileGapDirectRule() {
        super("BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is surrounded by the same value, fill the gap with the other value.",
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