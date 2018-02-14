package ui.boardview;

import model.gameboard.ElementData;

import java.awt.*;

public abstract class PuzzleElement
{
    protected int index;
    protected Point location;
    protected Dimension size;
    protected ElementData data;

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
        if(point.x >= location.x && point.x <= location.x + size.width &&
                point.y >= location.y && point.y <= location.y + size.height)
        {
            return true;
        }
        return false;
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
     * @return
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

    public ElementData getData()
    {
        return data;
    }

    public void setData(ElementData data)
    {
        this.data = data;
    }
}
