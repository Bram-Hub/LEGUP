package edu.rpi.legup.puzzle.kakurasu.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.kakurasu.KakurasuBoard;
import edu.rpi.legup.puzzle.kakurasu.KakurasuCell;
import edu.rpi.legup.puzzle.kakurasu.KakurasuClue;
import edu.rpi.legup.puzzle.kakurasu.KakurasuType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FinishWithFilledDirectRule extends DirectRule {
    public FinishWithFilledDirectRule() {
        super(
                "KAKU-BASC-0001",
                "Finish with Filled",
                "The only way to satisfy the clue in a row or column are these tiles.",
                "edu/rpi/legup/images/kakurasu/temp.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        KakurasuBoard initialBoard = (KakurasuBoard) transition.getParents().get(0).getBoard();
        KakurasuCell initCell = (KakurasuCell) initialBoard.getPuzzleElement(puzzleElement);
        KakurasuBoard finalBoard = (KakurasuBoard) transition.getBoard();
        KakurasuCell finalCell = (KakurasuCell) finalBoard.getPuzzleElement(puzzleElement);
        if (!(finalCell.getType() == KakurasuType.FILLED
                && initCell.getType() == KakurasuType.UNKNOWN)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell must be filled to apply this rule.";
        }

        if (isForced(initialBoard, initCell)) {
            return null;
        } else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be grass.";
        }
    }

    /**
     * Is used to determine if the cell being passed in is forced to exist in the
     * position it is in, given the board that was passed in
     *
     * @param board board to check the cell against
     * @param cell the cell whose legitimacy is in question
     * @return if the cell is forced to be at its position on the board
     */
    private boolean isForced(KakurasuBoard board, KakurasuCell cell) {
        Point loc = cell.getLocation();
        List<KakurasuCell> filledRow = board.getRowCol(loc.y, KakurasuType.FILLED, true);
        List<KakurasuCell> unknownRow = board.getRowCol(loc.y, KakurasuType.UNKNOWN, true);
        List<KakurasuCell> filledCol = board.getRowCol(loc.x, KakurasuType.FILLED, false);
        List<KakurasuCell> unknownCol = board.getRowCol(loc.x, KakurasuType.UNKNOWN, false);

        // Check if the remaining locations available must be filled to fulfill the clue value
        int rowSum = 0;
        for(KakurasuCell kc : filledRow) {
            rowSum += kc.getLocation().y;
        }
        for(KakurasuCell kc : unknownRow) {
            rowSum += kc.getLocation().y;
        }
        if(rowSum != board.getClue(board.getWidth(), loc.y).getData()) return false;

        int colSum = 0;
        for(KakurasuCell kc : filledCol) {
            colSum += kc.getLocation().x;
        }
        for(KakurasuCell kc : unknownCol) {
            colSum += kc.getLocation().x;
        }
        if(colSum != board.getClue(loc.x, board.getHeight()).getData()) return false;

        return true;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        KakurasuBoard KakurasuBoard = (KakurasuBoard) node.getBoard().copy();
        for (PuzzleElement element : KakurasuBoard.getPuzzleElements()) {
            KakurasuCell cell = (KakurasuCell) element;
            if (cell.getType() == KakurasuType.UNKNOWN && isForced(KakurasuBoard, cell)) {
                cell.setData(KakurasuType.FILLED);
                KakurasuBoard.addModifiedData(cell);
            }
        }
        if (KakurasuBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return KakurasuBoard;
        }
    }
}
