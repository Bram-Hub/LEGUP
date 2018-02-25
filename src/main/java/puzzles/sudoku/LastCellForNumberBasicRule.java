package puzzles.sudoku;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.BasicRule;

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
        int groupSize = sudokuBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = elementIndex / groupSize;
        int colIndex = elementIndex % groupSize;
        int groupNum = rowIndex / groupDim * groupDim + colIndex % groupDim;
        HashSet<Integer> region = new HashSet<>();
        HashSet<Integer> row = new HashSet<>();
        HashSet<Integer> column = new HashSet<>();
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = sudokuBoard.getCell(groupNum, i % groupDim, i / groupDim);
            if(cell.getValueInt() != 0 && !region.contains(cell.getValueInt()))
            {
                region.add(cell.getValueInt());
            }
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) sudokuBoard.getCell(i, colIndex);
            if(cell.getValueInt() != 0 && !row.contains(cell.getValueInt()))
            {
                row.add(cell.getValueInt());
            }
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) sudokuBoard.getCell(rowIndex, i);
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
