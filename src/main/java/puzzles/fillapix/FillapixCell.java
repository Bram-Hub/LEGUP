package puzzles.fillapix;

import model.gameboard.GridCell;

import java.awt.*;

public class FillapixCell extends GridCell
{
    private CellState state;

    public FillapixCell(int value, Point location) {
        super(value, location);
        state = CellState.UNKNOWN;
    }

    /**
     * Gets flag to indicate if this the state is black, white, or unknown
     *
     * @return CellState, whether the cell is black, white, or unknown
     */
    public CellState getState()
    {
        return state;
    }

    /**
     * Sets flag to indicate if this is part of the original board
     *
     * @param  state if the value is given for this cell, false otherwise
     */
    public void setState(CellState state)
    {
        this.state = state;
    }

    /**
     * Performs a deep copy on the FillapixCell
     *
     * @return a new copy of the FillapixCell that is independent of this one
     */
    @Override
    public FillapixCell copy()
    {
        FillapixCell cell = new FillapixCell(valueInt, (Point)location.clone());
        cell.setIndex(index);
        cell.setModifiable(isModifiable);
        cell.setState(state);
        return cell;
    }
}