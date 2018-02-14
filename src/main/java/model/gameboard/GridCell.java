package model.gameboard;

import java.awt.*;

public abstract class GridCell extends ElementData
{
    protected Point location;

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param valueString value String that represents the GridCell
     * @param location location of the GridCell in the grid
     */
    public GridCell(String valueString, Point location)
    {
        super(valueString);
        this.location = location;
    }

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param valueInt value int that represents the GridCell
     * @param location location of the GridCell in the grid
     */
    public GridCell(int valueInt, Point location)
    {
        super(valueInt);
        this.location = location;
    }

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param valueString value String that represents the GridCell
     * @param x x location of the GridCell in the grid
     * @param x y location of the GridCell in the grid
     */
    public GridCell(String valueString, int x, int y)
    {
        this(valueString, new Point(x, y));
    }

    /**
     * GridCell Constructor - creates a new GridCell at the specified location
     *
     * @param valueInt value int that represents the GridCell
     * @param x x location of the GridCell in the grid
     * @param x y location of the GridCell in the grid
     */
    public GridCell(int valueInt, int x, int y)
    {
        this(valueInt, new Point(x, y));
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
    public abstract GridCell copy();
}

