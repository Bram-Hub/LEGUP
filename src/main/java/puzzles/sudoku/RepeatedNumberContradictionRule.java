package puzzles.sudoku;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.ContradictionRule;

import java.util.HashSet;

public class RepeatedNumberContradictionRule extends ContradictionRule
{

    public RepeatedNumberContradictionRule()
    {
        super("Repeated Numbers",
                "Two identical numbers are placed in the same group.",
                "images/sudoku/RepeatedNumber.png");
    }

    /**
     * Checks whether the board has a contradiction using this rule
     *
     * @param board state of the board
     *
     * @return null if the board contains a contradiction, otherwise error message
     */
    @Override
    public String checkContradiction(Board board)
    {
        SudokuBoard sudokuBoard = (SudokuBoard)board;
        int groupSize = sudokuBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        for(int g = 0; g < groupSize; g++)
        {
            HashSet<Integer> numbers = new HashSet<>();
            for(int i = 0; i < groupSize; i++)
            {
                SudokuCell cell = sudokuBoard.getCell(g, i % groupDim, i / groupDim);
                if(cell.getValueInt() != 0)
                {
                    if(numbers.contains(cell.getValueInt()))
                    {
                        return null;
                    }
                    numbers.add(cell.getValueInt());
                }
            }
        }
        return "Board does not contain a contradiction";
    }

    /**
     * Checks whether the board has a contradiction at the specific element index using this rule
     *
     * @param board        state of the board
     * @param elementIndex index of the element
     *
     * @return null if the board contains a contradiction at the specified element,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, int elementIndex)
    {
        SudokuBoard sudokuBoard = (SudokuBoard)board;
        int groupSize = sudokuBoard.getWidth();
        int groupDim = (int)Math.sqrt(groupSize);
        int groupNum = (elementIndex / groupSize) / groupDim * groupDim + (elementIndex % groupSize) % groupDim * groupDim;
        HashSet<Integer> numbers = new HashSet<>();
        for(int i = 0; i < groupSize; i++)
        {
            numbers.add(sudokuBoard.getCell(groupNum, i % groupDim, i / groupDim).getValueInt());
        }
        if(numbers.contains(sudokuBoard.getElementData(elementIndex).getValueInt()))
        {
            return null;
        }
        else
        {
            return "Board does not contain a contradiction at index: " + elementIndex;
        }
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
        return checkContradiction(finalBoard);
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
        return checkContradictionAt(finalBoard, elementIndex);
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
