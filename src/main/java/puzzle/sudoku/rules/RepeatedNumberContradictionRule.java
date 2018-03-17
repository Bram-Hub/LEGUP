package puzzle.sudoku.rules;

import model.rules.ContradictionRule;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import puzzle.sudoku.SudokuBoard;
import puzzle.sudoku.SudokuCell;

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
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param transition transition to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    @Override
    public String checkContradiction(TreeTransition transition)
    {
        SudokuBoard sudokuBoard = (SudokuBoard)transition.getBoard();
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
     * Checks whether the transition has a contradiction at the specific element index using this rule
     *
     * @param transition   transition to check contradiction
     * @param elementIndex index of the element
     *
     * @return null if the transition contains a contradiction at the specified element,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, int elementIndex)
    {
        SudokuBoard sudokuBoard = (SudokuBoard)transition.getBoard();
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
