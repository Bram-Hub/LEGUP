package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class OneTileGapDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public OneTileGapDirectRule() {
        super(
                "BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is in between the same value, fill the gap with the other value.",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ThreeAdjacentContradictionRule contraRule = new ThreeAdjacentContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        if (!contraRule.checkOneTileGap(modified, binaryCell)) {
            return null;
        }

        return "Grouping of Three Ones or Zeros not found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
