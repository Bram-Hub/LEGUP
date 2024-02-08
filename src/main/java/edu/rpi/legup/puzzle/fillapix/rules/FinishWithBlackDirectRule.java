package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

public class FinishWithBlackDirectRule extends DirectRule {
    public FinishWithBlackDirectRule() {
        super("FPIX-BASC-0001",
                "Finish with Black",
                "The remaining unknowns around and on a cell must be black to satisfy the number",
                "edu/rpi/legup/images/fillapix/rules/FinishWithBlack.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        FillapixBoard board = (FillapixBoard) transition.getBoard();
        FillapixBoard parentBoard = (FillapixBoard) transition.getParents().get(0).getBoard();
        FillapixCell cell = (FillapixCell) board.getPuzzleElement(puzzleElement);
        FillapixCell parentCell = (FillapixCell) parentBoard.getPuzzleElement(puzzleElement);

        if (!(parentCell.getType() == FillapixCellType.UNKNOWN && cell.getType() == FillapixCellType.BLACK)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell must be black to be applicable with this rule.";
        }

        if (FillapixUtilities.isForcedBlack(parentBoard, cell)) {
            return null;
        }
        else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be black";
        }
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        FillapixBoard fillapixBoard = (FillapixBoard) node.getBoard().copy();
        for (PuzzleElement element : fillapixBoard.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) element;
            if (cell.getType() == FillapixCellType.UNKNOWN && FillapixUtilities.isForcedBlack((FillapixBoard) node.getBoard(), cell)) {
                cell.setCellType(FillapixCellType.BLACK);
                fillapixBoard.addModifiedData(cell);
            }
        }
        if (fillapixBoard.getModifiedData().isEmpty()) {
            return null;
        }
        else {
            return fillapixBoard;
        }
    }
}