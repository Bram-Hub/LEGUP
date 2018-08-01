package edu.rpi.legup.model.gameboard;

import java.awt.*;

public class GridCell<T> extends Element<T>
{
    protected Point location;

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param value data value that represents the GridCell
     * @param location location of the GridCell in the grid
     */
    public GridCell(T value, Point location)
    {
        super(value);
        this.location = location;
    }

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param value data value that represents the GridCell
     * @param x x location of the GridCell in the grid
     * @param y y location of the GridCell in the grid
     */
    public GridCell(T value, int x, int y)
    {
        this(value, new Point(x, y));
    }

    /**
     * Protect GridCell Constructor - creates a new GridCell with default values
     */
    protected GridCell()
    {
        super();
        this.location = new Point(-1,-1);
    }

    /**
     * Gets the location of the GridCell on the grid
     *
     * @return location of the GridCell
     */
    public Point getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the GridCell on the grid
     *
     * @param location location of the GridCell
     */
    public void setLocation(Point location)
    {
        this.location = location;
    }

    /**
     * Performs a deep copy on the GridCell
     *
     * @return a new copy of the GridCell that is independent of this one
     */
    public GridCell<T> copy()
    {
        GridCell<T> copy = new GridCell<>();
        copy.setIndex(index);
        copy.setData(data);
        copy.setModifiable(isModifiable);
        copy.setModified(isModified);
        copy.setGiven(isGiven);
        copy.setLocation((Point)location.clone());
        return copy;
    }
}

