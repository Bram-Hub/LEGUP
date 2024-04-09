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

import java.util.Set;

public class OneTileGapDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";
    public OneTileGapDirectRule() {
        super("BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is in between the same value, fill the gap with the other value.",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    boolean checkLeftRight(BinaryCell c, BinaryBoard board) {
        int x  = c.getLocation().x;
        int y  = c.getLocation().y;
        return board.getCell(x - 1, y).getType() != c.getType() || board.getCell(x + 1, y).getType() != c.getType();
    }
    boolean checkUpDown(BinaryCell c, BinaryBoard board) {
        int x  = c.getLocation().x;
        int y  = c.getLocation().y;
        return board.getCell(x, y - 1).getType() != c.getType() || board.getCell(x, y + 1).getType() != c.getType();
    }
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        BinaryBoard modified = origBoard.copy();

        //System.out.println("ORIG" + binaryCell.getData());
        //System.out.println("AFTER" + Math.abs(binaryCell.getData() - 1));
        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        PuzzleElement newP = binaryCell;

        System.out.println(contraRule.checkContradictionAt(modified, newP));

        if (contraRule.checkContradictionAt(modified, newP) == null) {
            return null;
        }
        modified.getPuzzleElement(puzzleElement).setData(Math.abs(binaryCell.getData() - 1));

        return "Grouping of Three Ones or Zeros not found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}