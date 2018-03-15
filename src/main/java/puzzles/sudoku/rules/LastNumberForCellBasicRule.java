package puzzles.sudoku.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.BasicRule;
import puzzles.sudoku.SudokuBoard;
import puzzles.sudoku.SudokuCell;

import javax.lang.model.element.Element;
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
        HashSet<Integer> numbers = new HashSet<>();
        for(int i = 1; i <= groupSize; i++)
        {
            numbers.add(i);
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = sudokuBoard.getCell(groupNum, i % groupDim, i / groupDim);
            numbers.remove(cell.getValueInt());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) sudokuBoard.getCell(i, colIndex);
            numbers.remove(cell.getValueInt());
        }
        for(int i = 0; i < groupSize; i++)
        {
            SudokuCell cell = (SudokuCell) sudokuBoard.getCell(rowIndex, i);
            numbers.remove(cell.getValueInt());
        }
        if(numbers.size() > 0)
        {
            return "The number at the index is not forced";
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
