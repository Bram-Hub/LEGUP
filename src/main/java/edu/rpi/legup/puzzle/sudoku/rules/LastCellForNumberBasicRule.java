package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.Set;

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
     * at the specific puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        SudokuBoard initialBoard = (SudokuBoard) transition.getParents().get(0).getBoard();
        SudokuBoard finalBoard = (SudokuBoard) transition.getBoard();

        SudokuCell cell = (SudokuCell) finalBoard.getPuzzleElement(puzzleElement);
        if(cell.getData() == 0)
        {
            return "cell is not forced at this index";
        }

        int size = initialBoard.getSize();

        Set<SudokuCell> region = initialBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = initialBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = initialBoard.getCol(cell.getLocation().x);

        boolean contains = false;
        if(region.size() == size - 1)
        {
            for(SudokuCell c : region)
            {
                if(cell.getData() == c.getData())
                {
                    contains = true;
                    break;
                }
            }
            if(!contains)
            {
                return null;
            }
        }
        if(row.size() == size - 1)
        {
            contains = false;
            for(SudokuCell c : row)
            {
                if(cell.getData() == c.getData())
                {
                    contains = true;
                    break;
                }
            }
            if(!contains)
            {
                return null;
            }
        }
        if(col.size() == size - 1)
        {
            contains = false;
            for(SudokuCell c : col)
            {
                if(cell.getData() == c.getData())
                {
                    contains = true;
                    break;
                }
            }
            if(!contains)
            {
                return null;
            }
        }
        return "cell is not forced at this index";
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
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement equivalent puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}
