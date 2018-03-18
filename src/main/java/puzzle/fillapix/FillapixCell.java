package puzzle.fillapix;

import model.gameboard.GridCell;

import java.awt.*;

public class FillapixCell extends GridCell
{
    private int state;
    private int clue;
    public static final int UNKNOWN = -50;
    public static final int BLACK = 20;
    public static final int WHITE = 30;

    public FillapixCell(int value, Point location) {
        super(value, location);
        state = (value/10)*10;
        clue = value%10;
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
    public static int getState(int value)
    {
        return (value/10)*10;
    }

    public int getClue() { return clue; }

    public boolean isUnknown() { return getState()==UNKNOWN; }
    public boolean isBlack() { return getState()==BLACK; }
    public boolean isWhite() { return getState()==WHITE; }

    public static boolean isUnknown(int valueInt) { return (getState(valueInt)==UNKNOWN); }
    public static boolean isBlack(int valueInt) { return getState(valueInt)==BLACK; }
    public static boolean isWhite(int valueInt) { return getState(valueInt)==WHITE; }

    /**
     * Sets flag to indicate if this is part of the original board
     *
     * @param  state if the value is given for this cell, false otherwise
     */
    public void setState(int state)
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