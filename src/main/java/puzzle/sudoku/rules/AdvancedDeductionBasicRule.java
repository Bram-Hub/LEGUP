package puzzle.sudoku.rules;

import model.rules.BasicRule;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import puzzle.sudoku.SudokuBoard;
import puzzle.sudoku.SudokuCell;

public class AdvancedDeductionBasicRule extends BasicRule
{

    public AdvancedDeductionBasicRule()
    {
        super("Advanced Deduction",
                "Use of group logic deduces more answers by means of forced by Location and forced by Deduction",
                "images/sudoku/AdvancedDeduction.png");
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

        SudokuCell cell = (SudokuCell) finalBoard.getElementData(elementIndex);
        int groupSize = initialBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = elementIndex / groupSize;
        int colIndex = elementIndex % groupSize;
        int relX = rowIndex / groupDim;
        int relY = colIndex % groupDim;
        int groupNum = rowIndex / groupDim * groupDim + colIndex / groupDim;
        boolean[][] possible = new boolean[groupDim][groupDim];
        for(int y = 0; y < groupDim; y++)
        {
            for(int x = 0; x < groupDim; x++)
            {
                SudokuCell c = initialBoard.getCell(groupNum, x, y);
                if(c.getValueInt() == cell.getValueInt() && x != relX && y != relY )
                {
                    return "Duplicate value in sub region";
                }
                possible[y][x] = c.getValueInt() == 0;
            }
        }
        for(int y = 0; y < groupDim; y++)
        {
            for(int x = 0; x < groupSize; x++)
            {
                SudokuCell r = (SudokuCell) initialBoard.getCell(x, (groupNum / groupDim) * groupDim + y);
                SudokuCell c = (SudokuCell) initialBoard.getCell((groupNum % groupDim) * groupDim + y, x);
                if(r.getValueInt() == cell.getValueInt())
                {
                    for(int i = 0; i < groupDim; i++)
                    {
                        possible[y][i] = false;
                    }
                }
                if(c.getValueInt() == cell.getValueInt())
                {
                    for(int i = 0; i < groupDim; i++)
                    {
                        possible[i][y] = false;
                    }
                }
            }
        }
        boolean isForced = false;
        for(int y = 0; y < groupDim; y++)
        {
            for(int x = 0; x < groupDim; x++)
            {
                if(possible[y][x] && !isForced)
                {
                    isForced = true;
                }
                else if(possible[y][x])
                {
                    return "Not forced";
                }
            }
        }
        if(!isForced)
        {
            return "Not forced";
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
