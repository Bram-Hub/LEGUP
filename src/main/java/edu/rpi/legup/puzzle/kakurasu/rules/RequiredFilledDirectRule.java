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
import java.util.ArrayList;
import java.util.List;

public class RequiredFilledDirectRule extends DirectRule {
    public RequiredFilledDirectRule() {
        super(
                "KAKU-BASC-0001",
                "Required Filled",
                "The only way to satisfy the clue in a row or column are these filled tiles.",
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
            return super.getInvalidUseOfRuleMessage() + ": This cell is not forced to be filled.";
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
        int rowValueRemaining = board.getClue(board.getWidth(), loc.y).getData();
        for(KakurasuCell kc : filledRow) {
            rowValueRemaining -= kc.getLocation().x + 1;
        }
        ArrayList<Integer> rowValues = new ArrayList<>();
        // Add all the unknown row values to the Arraylist except for the one being checked by the function
        for(KakurasuCell kc : unknownRow) {
            if(kc.getLocation() != loc) rowValues.add(kc.getLocation().x + 1);
        }
        // If the clue is not reachable without the current cell being filled, but is possible with it filled,
        // then that means the current cell is a required fill on this board
        if(!isReachable(rowValueRemaining, 0, rowValues) &&
            isReachable(rowValueRemaining-(loc.x+1), 0, rowValues)) {
            return true;
        }

        int colValueRemaining = board.getClue(loc.x, board.getHeight()).getData();
        for(KakurasuCell kc : filledCol) {
            colValueRemaining -= kc.getLocation().y + 1;
        }
        ArrayList<Integer> colValues = new ArrayList<>();
        // Add all the unknown col values to the Arraylist except for the one being checked by the function
        for(KakurasuCell kc : unknownCol) {
            if(kc.getLocation() != loc) colValues.add(kc.getLocation().y + 1);
        }
        // Return true if the clue is fulfilled, false if it isn't
        return (!isReachable(colValueRemaining, 0, rowValues) &&
                isReachable(colValueRemaining-(loc.y+1), 0, colValues));
    }

    /**
     * Helper function that checks if the target clue is reachable given a list of KakurasuCells
     * This function only works if the list of values are given in increasing index order (which it currently is)
     *
     * @param target The integer that we are trying to add up to, given the values
     * @param currentIndex The index of the next value that we are considering
     * @param values Values that we are given to try to sum up to the target
     * @return If it's possible to sum the values in a way to get the target value
     */
    private boolean isReachable(int target, int currentIndex, ArrayList<Integer> values) {
        if(target == 0) return true;
        if(target < 0 || currentIndex >= values.size()) return false;
        return (isReachable(target, currentIndex+1, values) ||
                isReachable(target - values.get(currentIndex), currentIndex+1, values));
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
