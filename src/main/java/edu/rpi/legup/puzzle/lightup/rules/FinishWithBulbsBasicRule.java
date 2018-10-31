package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.util.Set;

public class FinishWithBulbsBasicRule extends BasicRule {

    public FinishWithBulbsBasicRule() {
        super("Finish with Bulbs",
                "The remaining unknowns around a block must be bulbs to satisfy the number.",
                "edu/rpi/legup/images/lightup/rules/FinishWithBulbs.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        LightUpCell initCell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(initCell.getType() == LightUpCellType.UNKNOWN && finalCell.getType() == LightUpCellType.BULB)) {
            return "Modified cells must be bulbs";
        }

        Set<LightUpCell> adjCells = finalBoard.getAdj(finalCell);
        adjCells.removeIf(cell -> cell.getType() != LightUpCellType.NUMBER);
        if (adjCells.isEmpty()) {
            return "This cell is not adjacent to a numbered cell.";
        }

        LightUpBoard emptyCase = initialBoard.copy();
        emptyCase.getPuzzleElement(finalCell).setData(LightUpCellType.EMPTY.value);
        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
        for (LightUpCell c : adjCells) {
            if (tooFew.checkContradictionAt(emptyCase, c) == null) {
                return null;
            }
        }
        return "This cell is forced to be a bulb.";
    }

    private boolean isForced(LightUpBoard board, LightUpCell cell) {
        Set<LightUpCell> adjCells = board.getAdj(cell);
        adjCells.removeIf(c -> c.getType() != LightUpCellType.NUMBER);
        if (adjCells.isEmpty()) {
            return false;
        }

        LightUpBoard emptyCase = board.copy();
        emptyCase.getPuzzleElement(cell).setData(LightUpCellType.EMPTY.value);
        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
        for (LightUpCell c : adjCells) {
            if (tooFew.checkContradictionAt(emptyCase, c) == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard initialBoard = (LightUpBoard) node.getBoard();
        LightUpBoard lightUpBoard = (LightUpBoard) node.getBoard().copy();
        for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) element;
            if (cell.getType() == LightUpCellType.UNKNOWN && isForced(initialBoard, cell)) {
                cell.setData(LightUpCellType.BULB.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
