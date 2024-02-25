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
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";
    public OneTileGapDirectRule() {
        super("BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is in between the same value, fill the gap with the other value.",
                "FILL IN WITH IMAGE");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard iniitalBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryBoard finalBoard = (BinaryBoard) transition.getBoard();
        int boardDim = iniitalBoard.getWidth();
        BinaryCell cell = (BinaryCell) finalBoard.getPuzzleElement(puzzleElement);

        BinaryCell upOne = finalBoard.getCell(cell.getLocation().x, cell.getLocation().y+1);
        BinaryCell downOne = finalBoard.getCell(cell.getLocation().x, cell.getLocation().y-1);
        BinaryCell leftOne = finalBoard.getCell(cell.getLocation().x-1, cell.getLocation().y);
        BinaryCell rightOne = finalBoard.getCell(cell.getLocation().x+1, cell.getLocation().y);

        if ((upOne.getType() == BinaryType.ONE && downOne.getType() == BinaryType.ONE && cell.getType() == BinaryType.ONE) ||
                (upOne.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO && cell.getType() == BinaryType.ZERO) ||
                (leftOne.getType() == BinaryType.ONE && rightOne.getType() == BinaryType.ONE && cell.getType() == BinaryType.ONE) ||
                (leftOne.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO && cell.getType() == BinaryType.ZERO)) {
            return null;
        }
        return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}