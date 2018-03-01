package puzzles.sudoku;

import model.gameboard.GridCell;

import java.awt.*;

public class SudokuCell extends GridCell
{
    private boolean isGiven;

    public SudokuCell(int value, Point location)
    {
        super(value, location);
        isGiven = false;
    }

    /**
     * Gets flag to indicate if this is part of the original board
     *
     * @return true if the value is given for this cell, false otherwise
     */
    public boolean isGiven()
    {
        return isGiven;
    }

    /**
     * Sets flag to indicate if this is part of the original board
     *
     * @param isGiven true if the value is given for this cell, false otherwise
     */
    public void setGiven(boolean isGiven)
    {
        this.isGiven = isGiven;
    }

    /**
     * Performs a deep copy on the SudokuCell
     *
     * @return a new copy of the SudokuCell that is independent of this one
     */
    @Override
    public SudokuCell copy()
    {
        SudokuCell cell = new SudokuCell(valueInt, (Point)location.clone());
        cell.setIndex(index);
        cell.setModifiable(isModifiable);
        cell.setGiven(isGiven);
        return cell;
    }
}
