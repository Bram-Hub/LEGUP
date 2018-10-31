package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;

public class FinishWithWhiteBasicRule extends BasicRule {
    public FinishWithWhiteBasicRule() {
        super("Finish with White",
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
            return "This cell must be white to be applicable with this rule.";
        }

        if (isForcedWhite(parentBoard, cell)) {
            return null;
        } else {
            return "This cell is not forced to be white";
        }
    }

    private boolean isForcedWhite(FillapixBoard board, FillapixCell cell) {
        TooManyBlackCellsContradictionRule tooManyBlackCells = new TooManyBlackCellsContradictionRule();
        FillapixBoard blackCaseBoard = board.copy();
        FillapixCell blackCell = (FillapixCell) blackCaseBoard.getPuzzleElement(cell);
        blackCell.setType(FillapixCellType.BLACK);
        return tooManyBlackCells.checkContradictionAt(blackCaseBoard, cell) == null;
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
            if (cell.getType() == FillapixCellType.UNKNOWN && isForcedWhite((FillapixBoard) node.getBoard(), cell)) {
                cell.setType(FillapixCellType.WHITE);
                fillapixBoard.addModifiedData(cell);
            }
        }
        if (fillapixBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return fillapixBoard;
        }
    }
}