package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;

public class LastNumberForCellBasicRule extends BasicRule
{

    public LastNumberForCellBasicRule()
    {
        super("Last Number for Cell",
                "This is the only number left that can fit in the cell of a group.",
                "edu/rpi/legup/images/sudoku/forcedByDeduction.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParents().get(0).getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        int index = puzzleElement.getIndex();
        int groupSize = initialBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = index / groupSize;
        int colIndex = index % groupSize;
        int groupNum = rowIndex / groupDim * groupDim + colIndex % groupDim;
        HashSet<Integer> numbers = new HashSet<>();
        for(int i = 1; i <= groupSize; i++)
        {
            numbers.add(i);
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = initialBoard.getCell(groupNum, i % groupDim, i / groupDim);
            numbers.remove(cell.getData());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = initialBoard.getCell(i, colIndex);
            numbers.remove(cell.getData());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = initialBoard.getCell(rowIndex, i);
            numbers.remove(cell.getData());
        }
        if(numbers.size() > 1)
        {
            return "The number at the index is not forced";
        }
        else if(numbers.size() == 1 && numbers.iterator().next() != finalBoard.getPuzzleElement(puzzleElement).getData())
        {
            return "The number at the index is forced but not correct";
        }
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement equivalent puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}
