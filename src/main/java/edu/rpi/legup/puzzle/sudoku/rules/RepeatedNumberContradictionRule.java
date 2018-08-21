package edu.rpi.legup.puzzle.sudoku.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.sudoku.SudokuBoard;
import edu.rpi.legup.puzzle.sudoku.SudokuCell;

import java.util.HashSet;
import java.util.Set;

public class RepeatedNumberContradictionRule extends ContradictionRule
{

    public RepeatedNumberContradictionRule()
    {
        super("Repeated Numbers",
                "Two identical numbers are placed in the same group.",
                "edu/rpi/legup/images/sudoku/RepeatedNumber.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check contradiction
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        SudokuBoard sudokuBoard = (SudokuBoard)transition.getBoard();
        SudokuCell cell = (SudokuCell) sudokuBoard.getPuzzleElement(puzzleElement);
        if(cell.getData() == 0)
        {
            return "Does not contain a contradiction at this index";
        }

        Set<SudokuCell> region = sudokuBoard.getRegion(cell.getGroupIndex());
        Set<SudokuCell> row = sudokuBoard.getRow(cell.getLocation().y);
        Set<SudokuCell> col = sudokuBoard.getCol(cell.getLocation().x);

        Set<Integer> regionDup = new HashSet<>();
        Set<Integer> rowDup = new HashSet<>();
        Set<Integer> colDup = new HashSet<>();

        for(SudokuCell c : region)
        {
            if(regionDup.contains(c.getData()))
            {
                return null;
            }
            regionDup.add(c.getData());
        }

        for(SudokuCell c : row)
        {
            if(rowDup.contains(c.getData()))
            {
                return null;
            }
            rowDup.add(c.getData());
        }

        for(SudokuCell c : col)
        {
            if(colDup.contains(c.getData()))
            {
                return null;
            }
            colDup.add(c.getData());
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
