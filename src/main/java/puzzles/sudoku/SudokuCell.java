package puzzles.sudoku;

import model.gameboard.GridCell;

import java.awt.*;

public class SudokuCell extends GridCell
{
    public SudokuCell(int value, Point location)
    {
        super(value, location);

    }

    /**
     * Performs a deep copy on the GridCell
     *
     * @return a new copy of the GridCell that is independent of this one
     */
    @Override
    public GridCell copy()
    {
        return null;
    }
}
