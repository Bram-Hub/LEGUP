package puzzle.sudoku.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.BasicRule;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import puzzle.sudoku.Sudoku;
import puzzle.sudoku.SudokuBoard;
import puzzle.sudoku.SudokuCell;

import java.util.HashSet;

public class LastCellForNumberBasicRule extends BasicRule
{
    public LastCellForNumberBasicRule()
    {
        super("Last Cell for Number",
                "This is the only cell open in its group for some number.",
                "images/sudoku/forcedByElimination.png");
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
    public String checkRuleAt(TreeTransition transition, int elementIndex)
    {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParentNode().getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        int groupSize = initialBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = elementIndex / groupSize;
        int colIndex = elementIndex % groupSize;
        int groupNum = rowIndex / groupDim * groupDim + colIndex % groupDim;
        HashSet<Integer> region = new HashSet<>();
        HashSet<Integer> row = new HashSet<>();
        HashSet<Integer> column = new HashSet<>();
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = initialBoard.getCell(groupNum, i % groupDim, i / groupDim);
            if(cell.getValueInt() != 0 && !region.contains(cell.getValueInt()))
            {
                region.add(cell.getValueInt());
            }
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) initialBoard.getCell(i, colIndex);
            if(cell.getValueInt() != 0 && !row.contains(cell.getValueInt()))
            {
                row.add(cell.getValueInt());
            }
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) initialBoard.getCell(rowIndex, i);
            if(cell.getValueInt() != 0 && !column.contains(cell.getValueInt()))
            {
                column.add(cell.getValueInt());
            }
        }
        if(region.size() != groupSize - 1 || row.size() != groupSize - 1 || column.size() != groupSize - 1)
        {
            return "The group has more numbers left to fill in";
        }
        else if(region.contains(finalBoard.getElementData(elementIndex).getValueInt()) ||
                row.contains(finalBoard.getElementData(elementIndex).getValueInt()) ||
                column.contains(finalBoard.getElementData(elementIndex).getValueInt()))
        {
            return "The group has a duplicate number contained in it";
        }
        else
        {
            return null;
        }
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
