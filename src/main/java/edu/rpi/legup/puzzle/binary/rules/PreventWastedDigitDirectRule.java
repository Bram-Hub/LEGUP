package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class PreventWastedDigitDirectRule extends DirectRule  {

    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public PreventWastedDigitDirectRule() {
        super(
                "BINA-BASC-0004",
                "Prevent Wasted Digit",
                "If somewhere in a row/col a future trio must be blocked, insert th",
                "edu/rpi/legup/images/binary/rules/PreventWastedDigitDirectRule.png");
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        WastedDigitContradictionRule contraRule = new WastedDigitContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        if (contraRule.checkContradictionAt(modified, binaryCell) == null) {
            return null;
        }

        return "Wasted Digit Found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }


}
