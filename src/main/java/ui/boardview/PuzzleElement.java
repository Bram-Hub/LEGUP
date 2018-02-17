package ui.boardview;

import model.gameboard.ElementData;

import javax.swing.*;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;

public abstract class PuzzleElement
{
    protected int index;
    protected Point location;
    protected Dimension size;
    protected ElementData data;

    /**
     * PuzzleElement Constructor - creates a puzzle element view
     *
     * @param data element data to which the puzzle element uses to draw
     */
    public PuzzleElement(ElementData data)
    {
        this.data = data;
    }

    /**
     * Determines if the specified point is within the PuzzleElement
     *
     * @param point point to check
     * @return true if the point is within the PuzzleElement, false otherwise
     */
    public boolean isWithinBounds(Point point)
    {
        return point.x >= location.x && point.x <= location.x + size.width &&
                point.y >= location.y && point.y <= location.y + size.height;
    }

    /**
     * Draws the puzzle element on the screen
     *
     * @param graphics2D graphics2D object used for drawing
     */
    public abstract void draw(Graphics2D graphics2D);

    /**
     * Gets the index of the PuzzleElement
     *
     * @return index of the PuzzleElement
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index of the PuzzleElement
     *
     * @param index index of the PuzzleElement
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Gets the location of the PuzzleElement
     *
     * @return location of the PuzzleElement
     */
    public Point getLocation()
    {
        return location;
    }

    /**
     * Sets the location of the PuzzleElement
     *
     * @param location location of the PuzzleElement
     */
    public void setLocation(Point location)
    {
        this.location = location;
    }

    /**
     * Gets the size of the PuzzleElement
     *
     * @return size of the PuzzleElement
     */
    public Dimension getSize()
    {
        return size;
    }

    /**
     * Sets the size of the PuzzleElement
     *
     * @param size size of the PuzzleElement
     */
    public void setSize(Dimension size)
    {
        this.size = size;
    }

    /**
     * Gets the ElementData associated with this view
     *
     * @return ElementData associated with this view
     */
    public ElementData getData()
    {
        return data;
    }

    /**
     * Sets the ElementData associated with this view
     *
     * @param data ElementData associated with this view
     */
    public void setData(ElementData data)
    {
        this.data = data;
    }
}
