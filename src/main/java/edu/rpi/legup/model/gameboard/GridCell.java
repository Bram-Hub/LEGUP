package edu.rpi.legup.model.gameboard;

import java.awt.*;

/**
 * GridCell represents a cell within a grid-based board. It holds data of type T and tracks its
 * location on the board using a {@link Point}. The class extends from PuzzleElement and supports
 * deep copying of the grid cell.
 *
 * @param <T> the type of data held by the GridCell
 */
public class GridCell<T> extends PuzzleElement<T> {
    protected Point location;

    /**
     * GridCell Constructor creates a grid cell at the specified location given as a {@link Point}
     *
     * @param value data value that represents the grid cell
     * @param location location on the board
     */
    public GridCell(T value, Point location) {
        super(value);
        this.location = location;
    }

    /**
     * GridCell Constructor creates a grid cell at the specified location given as x,y pair
     *
     * @param value data value that represents the grid cell
     * @param x x location
     * @param y y location
     */
    public GridCell(T value, int x, int y) {
        this(value, new Point(x, y));
    }

    /**
     * Gets the location of the grid cell on the board
     *
     * @return location of the grid cell
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the location of the grid cell on the board
     *
     * @param location location of the grid cell
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Performs a deep copy on the grid cell
     *
     * @return a new copy of the grid cell that is independent of this one
     */
    public GridCell<T> copy() {
        GridCell<T> copy = new GridCell<>(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setModified(isModified);
        copy.setGiven(isGiven);
        return copy;
    }

    public boolean isEqual(GridCell<T> other) {
        return location.equals(other.location) && data.equals(other.data);
    }
}
