package puzzle.sudoku.rules;

import model.rules.BasicRule;
import model.tree.TreeTransition;
import puzzle.sudoku.SudokuBoard;
import puzzle.sudoku.SudokuCell;

import java.util.HashSet;

public class LastNumberForCellBasicRule extends BasicRule
{

    public LastNumberForCellBasicRule()
    {
        super("Last Number for Cell",
                "This is the only number left that can fit in the cell of a group.",
                "images/sudoku/forcedByDeduction.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition transition to check
     * @param elementIndex index of the element
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParentNode().getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        int groupSize = initialBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = elementIndex / groupSize;
        int colIndex = elementIndex % groupSize;
        int groupNum = rowIndex / groupDim * groupDim + colIndex % groupDim;
        HashSet<Integer> numbers = new HashSet<>();
        for(int i = 1; i <= groupSize; i++)
        {
            numbers.add(i);
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = initialBoard.getCell(groupNum, i % groupDim, i / groupDim);
            numbers.remove(cell.getValueInt());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) initialBoard.getCell(i, colIndex);
            numbers.remove(cell.getValueInt());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) initialBoard.getCell(rowIndex, i);
            numbers.remove(cell.getValueInt());
        }
        if(numbers.size() > 0)
        {
            return "The number at the index is not forced";
        }
        else if(numbers.size() == 1 && numbers.iterator().next() != finalBoard.getElementData(elementIndex).getValueInt())
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
     * specific element index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param elementIndex
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex)
    {
        return false;
    }
}
