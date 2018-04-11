package model.gameboard;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GridBoard extends Board
{
    private final static Logger LOGGER = Logger.getLogger(GridBoard.class.getName());

    protected Dimension dimension;

    /**
     * GridBoard Constructor - models a GridBoard
     *
     * @param width width of the GridBoard
     * @param height height of the GridBoard
     */
    public GridBoard(int width, int height)
    {
        this.dimension = new Dimension(width, height);

        for(int i = 0; i < width * height; i++)
        {
            elementData.add(null);
        }
    }

    /**
     * GridBoard Constructor - models a GridBoard
     *
     * @param size width and height of the GridBoard
     */
    public GridBoard(int size)
    {
        this(size, size);
    }

    /**
     * Gets a GridCell from the board
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @return GridCell at location (x,y)
     */
    public GridCell getCell(int x, int y)
    {
        if(x * dimension.width + y >= elementData.size() || x >= dimension.width || y >= dimension.height || x < 0 || y < 0)
        {
            return null;
        }
        return (GridCell)elementData.get(y * dimension.width + x);
    }

    /**
     * Sets the GridCell at the location (x,y)
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @param cell GridCell to set at location (x,y)
     */
    public void setCell(int x, int y, GridCell cell)
    {
        if(x * dimension.width + y >= elementData.size() && x < dimension.width && y < dimension.height && x >= 0 && y >= 0)
        {
            return;
        }
        elementData.set(y * dimension.width + x, cell);
    }

    /**
     * Gets the width of the board
     *
     * @return width of the board
     */
    public int getWidth()
    {
        return dimension.width;
    }

    /**
     * Gets the height of the board
     *
     * @return height
     */
    public int getHeight()
    {
        return dimension.height;
    }

    /**
     * Gets the dimension of the grid board
     *
     * @return the dimension of the grid board
     */
    public Dimension getDimension()
    {
        return dimension;
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    public GridBoard copy()
    {
        GridBoard newGridBoard = new GridBoard(this.dimension.width, this.dimension.height);
        for(int x = 0; x < this.dimension.width; x++)
        {
            for(int y = 0; y < this.dimension.height; y++)
            {
                newGridBoard.setCell(x, y, getCell(x, y).copy());
            }
        }
        return newGridBoard;
    }
}