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
        BinaryBoard destBoard = (BinaryBoard) transition.getBoard();
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new ThreeAdjacentContradictionRule();

        Set<PuzzleElement> changedCells = destBoard.getModifiedData();
        for (PuzzleElement p : changedCells) {
            BinaryCell c = (BinaryCell) destBoard.getPuzzleElement(p);

            int x = c.getLocation().x;
            int y = c.getLocation().y;
            boolean xOutOfBounds = false;
            boolean yOutOfBounds = false;
            if ((x - 1 < 0 || x + 1 >= destBoard.getWidth())) {
                xOutOfBounds = true;
            }
            if ((y - 1 < 0 || y + 1 >= destBoard.getHeight())) {
                yOutOfBounds = true;
            }
            if (xOutOfBounds && yOutOfBounds) {
                return "Corner of cell cannot have a one tile gap solution";
            }
            if (xOutOfBounds && !yOutOfBounds) {
                if (!checkUpDown(c, destBoard)) {
                    return "Not a valid one tile gap";
                }
            }
            System.out.println(x + " " + y);
            System.out.println(c.getType());
            if (c.getType() != BinaryType.UNKNOWN) {
                if (destBoard.getCell(x-1, y).getType() == c.getType() &&
                        destBoard.getCell(x+1, y).getType() == c.getType() ||
                        (destBoard.getCell(x, y-1).getType() == c.getType() &&
                        destBoard.getCell(x, y+1).getType() == c.getType())) {
                    return "Grouping of Three Ones or Zeros found";
                }
            }
        }
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}