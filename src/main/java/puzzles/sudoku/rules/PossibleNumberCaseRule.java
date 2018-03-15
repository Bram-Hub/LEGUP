package puzzles.sudoku.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import puzzles.sudoku.SudokuBoard;

import java.util.ArrayList;

public class PossibleNumberCaseRule extends CaseRule
{

    public PossibleNumberCaseRule()
    {
        super("Possible Numbers for Cell",
                "An empty cell has a limited set of possible numbers that can fill it.",
                "images/sudoku/PossibleValues.png");
    }

    @Override
    public Board getCaseBoard(Board board)
    {
        return null;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board        the current board state
     * @param elementIndex element to determine the possible cases for
     *
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, int elementIndex)
    {
        ArrayList<Board> cases = new ArrayList<>();
        SudokuBoard sudokuBoard = (SudokuBoard)board;
        ArrayList<Integer> values = new ArrayList<>();

        int groupSize = sudokuBoard.getWidth();
        for(int i = 1; i <= groupSize; i++)
        {
            values.add(i);
        }

        int groupDim = (int)Math.sqrt(groupSize);
        int rowIndex = elementIndex / groupSize;
        int colIndex = elementIndex % groupSize;
        int groupNum = rowIndex / groupDim * groupDim + colIndex / groupDim;

        for(int y = 0; y < groupDim; y++)
        {
            for(int x = 0; x < groupDim; x++)
            {
                values.remove(sudokuBoard.getCell(groupNum, x, y).getValueInt());
            }
        }

        for(int val: values)
        {
            Board caseBoard = sudokuBoard.copy();
            ElementData data = sudokuBoard.getElementData(elementIndex).copy();
            data.setValueInt(val);
            caseBoard.setElementData(elementIndex, data);
            cases.add(caseBoard);
        }

        return cases;
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
