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

public class SurroundPairDirectRule extends DirectRule {

    public SurroundPairDirectRule() {
        super("BINA-BASC-0001",
                "Surround Pair",
                "If two adjacent tiles have the same value, surround the tiles with the other value.",
                "edu/rpi/legup/images/binary/rules/SurroundPairDirectRule.png");
    }
    
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard board = (BinaryBoard) transition.getBoard();
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();


        BinaryCell cell = (BinaryCell) board.getPuzzleElement(puzzleElement);   

        if (cell.getType() == BinaryType.UNKNOWN) {
            return "Only ONE or ZERO cells are allowed for this rule!";
        }

            BinaryBoard modified = origBoard.copy();
            modified.getPuzzleElement(puzzleElement).setData(BinaryType.WHITE.toValue());
            if (contraRule.checkContradictionAt(modified, puzzleElement) != null) {
                return "Black cells must be placed in a region of black cells!";
            }
            return null;
        
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }

}