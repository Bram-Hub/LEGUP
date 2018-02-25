package model.gameboard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GridBoard extends Board
{
    private final static Logger LOGGER = Logger.getLogger(GridBoard.class.getName());

    protected int width;
    protected int height;

    /**
     * GridBoard Constructor - models a GridBoard
     *
     * @param width width of the GridBoard
     * @param height height of the GridBoard
     */
    public GridBoard(int width, int height)
    {
        this.width = width;
        this.height = height;

        for(int i = 0; i < width * height; i++)
        {
            elementData.add(null);
        }
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
        if(x * width + y >= elementData.size() || x >= width || y >= height || x < 0 || y < 0)
        {
            LOGGER.log(Level.SEVERE, "Trying to call getCell with index values - " +
                    "(" + x + ", " + y + "), which are out of range");
            return null;
        }
        return (GridCell)elementData.get(y * width + x);
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
        if(x * width + y >= elementData.size() && x < width && y < height && x >= 0 && y >= 0)
        {
            LOGGER.log(Level.SEVERE, "Trying to call setCell with index values - " +
                    "(" + x + ", " + y + "), which are out of range");
            return;
        }
        elementData.set(y * width + x, cell);
    }

    /**
     * Gets the width of the board
     *
     * @return width of the board
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Gets the height of the board
     *
     * @return height
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    public GridBoard copy()
    {
        GridBoard newGridBoard = new GridBoard(this.width, this.height);
        for(int x = 0; x < this.width; x++)
        {
            for(int y = 0; y < this.height; y++)
            {
                newGridBoard.setCell(x, y, getCell(x, y).copy());
            }
        }
        return newGridBoard;
    }

}