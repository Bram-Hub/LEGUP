package edu.rpi.legup.puzzle.kakurasu.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.kakurasu.KakurasuBoard;
import edu.rpi.legup.puzzle.kakurasu.KakurasuCell;
import edu.rpi.legup.puzzle.kakurasu.KakurasuType;

import java.awt.*;
import java.util.List;

public class RequiredEmptyDirectRule extends DirectRule {
    public RequiredEmptyDirectRule() {
        super(
                "KAKU-BASC-0002",
                "Required Empty",
                "The only way to satisfy the clue in a row or column are these empty tiles.",
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
        if (!(finalCell.getType() == KakurasuType.EMPTY
                && initCell.getType() == KakurasuType.UNKNOWN)) {
            return super.getInvalidUseOfRuleMessage() + ": This cell must be empty to apply this rule.";
        }

        if (isForced(initialBoard, initCell)) {
            return null;
        } else {
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be empty.";
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
        List<KakurasuCell> filledCol = board.getRowCol(loc.x, KakurasuType.FILLED, false);

        // Check if the remaining locations available must be filled to fulfill the clue value
        int rowSum = 0;
        for(KakurasuCell kc : filledRow) {
            rowSum += kc.getLocation().x + 1;
        }
        // If setting this cell to filled causes the clue to fail, this cell must be empty
        if(rowSum + loc.x + 1 > board.getClue(board.getWidth(), loc.y).getData()) return true;

        int colSum = 0;
        for(KakurasuCell kc : filledCol) {
            colSum += kc.getLocation().y + 1;
        }
        // Return true if the clue is exceeded if this cell is filled,
        // Return false if setting the cell to filled keeps the col total to under the clue value
        return (colSum + loc.y + 1> board.getClue(loc.x, board.getHeight()).getData());
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
                cell.setData(KakurasuType.EMPTY);
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
