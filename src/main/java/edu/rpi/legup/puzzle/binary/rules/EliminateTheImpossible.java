package edu.rpi.legup.puzzle.binary.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

public class EliminateTheImpossible extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public EliminateTheImpossible() {
        super(
                "BINA-BASC-0003",
                "Eliminate The Impossible",
                "If three adjacent empty cells are open, prevents a trio of numbers to exist",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        return "Grouping of Three Ones or Zeros not found TEST";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
