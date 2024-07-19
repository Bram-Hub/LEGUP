package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class SaveBlockerDirectRule extends DirectRule  {

    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public SaveBlockerDirectRule() {
        super(
                "BINA-BASC-0004",
                "Save Blocker",
                "If a future trio could appear in this row/col, save the digit that could block that trio",
                "edu/rpi/legup/images/binary/rules/SaveBlockerDirectRule.png");
    }

    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        WastedBlockerContradictionRule contraRule = new WastedBlockerContradictionRule();
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
