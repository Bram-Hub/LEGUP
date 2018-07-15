package puzzle.sudoku.rules;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.CaseRule;
import model.tree.TreeTransition;
import puzzle.sudoku.SudokuBoard;

import java.util.ArrayList;

public class PossibleNumberCaseRule extends CaseRule
{

    public PossibleNumberCaseRule()
    {
        super("Possible Numbers for Cell",
                "An empty cell has a limited set of possible numbers that can fill it.",
                "images/sudoku/PossibleValues.png");
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
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
}
