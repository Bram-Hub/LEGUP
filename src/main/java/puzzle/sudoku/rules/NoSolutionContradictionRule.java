package puzzle.sudoku.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.sudoku.SudokuBoard;
import puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class NoSolutionContradictionRule extends ContradictionRule
{

    public NoSolutionContradictionRule()
    {
        super("No Solution for Cell",
                "Process of elimination yields no valid numbers for an empty cell.",
                "images/sudoku/NoSolution.png");
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
        SudokuCell cell = (SudokuCell) sudokuBoard.getElementData(elementIndex);
        if(cell.getValueInt() != 0)
        {
            return "Does not contain a contradiction at this index";
        }

        int groupSize = sudokuBoard.getSize();

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = sudokuBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = sudokuBoard.getCol(cell.getLocation().x);
        Set<Integer> solution = new HashSet<>();
        for(int i = 1; i <= groupSize; i++)
        {
            solution.add(i);
        }

        for(SudokuCell c : region)
        {
            solution.remove(c.getValueInt());
        }
        for(SudokuCell c : row)
        {
            solution.remove(c.getValueInt());
        }
        for(SudokuCell c : col)
        {
            solution.remove(c.getValueInt());
        }

        if(solution.isEmpty())
        {
            return null;
        }

        return "Does not contain a contradiction at this index";
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
