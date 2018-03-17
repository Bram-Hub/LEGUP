package puzzle.sudoku;

import model.gameboard.GridCell;

import java.awt.*;

public class SudokuCell extends GridCell
{
    public SudokuCell(int value, Point location)
    {
        super(value, location);
    }

    /**
     * Performs a deep copy on the SudokuCell
     *
     * @return a new copy of the SudokuCell that is independent of this one
     */
    @Override
    public SudokuCell copy()
    {
        SudokuCell copy = new SudokuCell(valueInt, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
