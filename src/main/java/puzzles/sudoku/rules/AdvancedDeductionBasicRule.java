package puzzles.sudoku.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.BasicRule;
import puzzles.sudoku.SudokuBoard;
import puzzles.sudoku.SudokuCell;

import java.util.HashSet;

public class AdvancedDeductionBasicRule extends BasicRule
{

    public AdvancedDeductionBasicRule()
    {
        super("Proved by Advanced Deduction",
                "Use of group logic deduces more answers by means of forced by Location and forced by Deduction",
                "images/sudoku/AdvancedDeduction.png");
    }

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard using this rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard   final state of the board
     *
     * @return null if the finalBoard logically follow from the initializeBoard, otherwise error message
     */
    @Override
    public String checkRule(Board initialBoard, Board finalBoard)
    {
        for(ElementData data: initialBoard.getElementData())
        {
            String checkStr = checkRuleAt(initialBoard, finalBoard, data.getIndex());
            if(checkStr != null)
                return checkStr;
        }
        return null;
    }

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard
     * at the specific element index using this rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard   final state of the board
     * @param elementIndex index of the element
     *
     * @return null if the finalBoard logically follow from the initializeBoard at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleAt(Board initialBoard, Board finalBoard, int elementIndex)
    {
        if(initialBoard.getElementData(elementIndex).getValueInt() == finalBoard.getElementData(elementIndex).getValueInt())
        {
            return null;
        }

        SudokuBoard sudokuBoard = (SudokuBoard)initialBoard;
        SudokuCell cell = (SudokuCell) finalBoard.getElementData(elementIndex);
        int groupSize = sudokuBoard.getWidth();
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
                SudokuCell c = sudokuBoard.getCell(groupNum, x, y);
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
                SudokuCell r = (SudokuCell) sudokuBoard.getCell(x, (groupNum / groupDim) * groupDim + y);
                SudokuCell c = (SudokuCell) sudokuBoard.getCell((groupNum % groupDim) * groupDim + y, x);
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
     * Checks whether the finalBoard logically follows from the initializeBoard using this rule
     * and if so will perform the default application of the rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard   final state of the board
     *
     * @return true if the finalBoard logically follow from the initializeBoard and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(Board initialBoard, Board finalBoard)
    {
        return false;
    }

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard at the
     * specific element index using this rule and if so will perform the default application of the rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard   final state of the board
     * @param elementIndex
     *
     * @return true if the finalBoard logically follow from the initializeBoard and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(Board initialBoard, Board finalBoard, int elementIndex)
    {
        return false;
    }
}
