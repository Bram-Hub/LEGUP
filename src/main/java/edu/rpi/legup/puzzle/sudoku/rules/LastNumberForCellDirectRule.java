package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;
import java.util.HashSet;

public class LastNumberForCellDirectRule extends DirectRule {

    public LastNumberForCellDirectRule() {
        super(
                "SUDO-BASC-0003",
                "Last Number for Cell",
                "This is the only number left that can fit in the cell of a group.",
                "edu/rpi/legup/images/sudoku/forcedByDeduction.png");
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
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParents().get(0).getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        // Assign basics
        int groupSize = initialBoard.getWidth();
        int groupDim = (int) Math.sqrt(groupSize);

        // Get position info
        int index = puzzleElement.getIndex();
        int rowIndex = index / groupSize;
        int colIndex = index % groupSize;
        int groupNum = (rowIndex / groupDim) * groupDim + (colIndex / groupDim);

        // Create hashset of all numbers
        HashSet<Integer> numbers = new HashSet<>();
        for (int i = 1; i <= groupSize; i++) {
            numbers.add(i);
        }

        // Run through region, row, col to see contradicitng numbers
        for (int i = 0; i < groupSize; i++) {
            SudokuCell cell = initialBoard.getCell(groupNum, i % groupDim, i / groupDim);
            numbers.remove(cell.getData());
        }
        for (int i = 0; i < groupSize; i++) {
            SudokuCell cell = initialBoard.getCell(i, rowIndex);
            numbers.remove(cell.getData());
        }
        for (int i = 0; i < groupSize; i++) {
            SudokuCell cell = initialBoard.getCell(colIndex, i);
            numbers.remove(cell.getData());
        }

        // Check if plausible
        if (numbers.size() > 1) {
            return super.getInvalidUseOfRuleMessage() + ": The number at the index is not forced";
        } else {
            if (numbers.size() == 1
                    && numbers.iterator().next()
                            != finalBoard.getPuzzleElement(puzzleElement).getData()) {
                return super.getInvalidUseOfRuleMessage()
                        + ": The number at the index is forced but not correct";
            }
        }
        if (numbers.toArray(new Integer[1])[0] == puzzleElement.getData()) {
            return null;
        }
        return super.getInvalidUseOfRuleMessage()
                + ": The number at the index is forced but not correct";
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
        return null;
    }
}
