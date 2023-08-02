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

public class FinishWithWhiteDirectRule extends DirectRule {
    public FinishWithWhiteDirectRule() {
        super("FPIX-BASC-0002",
                "Finish with White",
                "The remaining unknowns around and on a cell must be white to satisfy the number",
                "edu/rpi/legup/images/fillapix/rules/FinishWithWhite.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        FillapixBoard board = (FillapixBoard) transition.getBoard();
        FillapixBoard parentBoard = (FillapixBoard) transition.getParents().get(0).getBoard();
        FillapixCell cell = (FillapixCell) board.getPuzzleElement(puzzleElement);
        FillapixCell parentCell = (FillapixCell) parentBoard.getPuzzleElement(puzzleElement);

        if (!(parentCell.getType() == FillapixCellType.UNKNOWN && cell.getType() == FillapixCellType.WHITE)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell must be white to be applicable with this rule";
        }

        if (FillapixUtilities.isForcedWhite(parentBoard, cell)) {
            return null;
        }
        else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be white";
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
            if (cell.getType() == FillapixCellType.UNKNOWN && FillapixUtilities.isForcedWhite((FillapixBoard) node.getBoard(), cell)) {
                cell.setCellType(FillapixCellType.WHITE);
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