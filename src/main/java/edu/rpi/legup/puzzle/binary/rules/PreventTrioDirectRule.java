package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class PreventTrioDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public PreventTrioDirectRule() {
        super(
                "BINA-BASC-0001",
                "Prevent Trio",
                "If a trio contradiction state could appear, use the opposite digit to prevent the trio",
                "edu/rpi/legup/images/binary/rules/PreventTrioDirectRule.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        TrioContradictionRule contraRule = new TrioContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        if (contraRule.checkContradictionAt(modified, binaryCell) == null) {
            return null;
        }

        return "Trio Found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}