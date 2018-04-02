package puzzle.fillapix;

import model.gameboard.GridCell;

import java.awt.*;

public class FillapixCell extends GridCell
{
    public static final int UNKNOWN = -50;
    public static final int BLACK = 20;
    public static final int WHITE = 30;
    private int state;
    private int clue;

    public FillapixCell(int value, Point location)
    {
        super(value, location);
        state = (value / 10) * 10;
        clue = -1;
        if(FillapixCell.isGiven(value))
        {
            clue = value % 10;
            setGiven(true);
        }
    }

    public static boolean isGiven(int value)
    {
        return value != -1 && value != 19 && value != 49;
    }

    public static int getState(int value)
    {
        return (value / 10) * 10;
    }

    public static boolean isUnknown(int valueInt)
    {
        return (getState(valueInt) == 0);
    }

    public static boolean isBlack(int valueInt)
    {
        return getState(valueInt) == 20 || getState(valueInt) == 10;
    }

    public static boolean isWhite(int valueInt)
    {
        return getState(valueInt) == 50 || getState(valueInt) == 40;
    }

    public int getClue()
    {
        return clue;
    }

    public boolean isGiven()
    {
        return isGiven;
    }

    /**
     * Gets flag to indicate if this the state is black, white, or unknown
     *
     * @return CellState, whether the cell is black, white, or unknown
     */
    public int getState()
    {
        return state;
    }

    /**
     * Sets flag to indicate if this is part of the original board
     *
     * @param state if the value is given for this cell, false otherwise
     */
    public void setState(int state)
    {
        this.state = state;
    }

    public boolean isUnknown()
    {
        return isUnknown(getState());
    }

    public boolean isBlack()
    {
        return isBlack(getState());
    }

    public boolean isWhite()
    {
        return isWhite(getState());
    }

    public boolean hasSameState(FillapixCell cell)
    {
        return ((this.isUnknown() && cell.isUnknown()) || (this.isBlack() && cell.isBlack()) || (this.isWhite() && cell.isWhite()));
    }

    /**
     * Performs a deep copy on the FillapixCell
     *
     * @return a new copy of the FillapixCell that is independent of this one
     */
    @Override
    public FillapixCell copy()
    {
        FillapixCell cell = new FillapixCell(valueInt, (Point) location.clone());
        cell.setIndex(index);
        cell.setModifiable(isModifiable);
        cell.setState(state);
        return cell;
    }
}