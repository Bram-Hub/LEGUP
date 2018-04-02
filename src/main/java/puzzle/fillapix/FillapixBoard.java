package puzzle.fillapix;

import model.gameboard.GridBoard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FillapixBoard extends GridBoard
{
    private final static Logger LOGGER = Logger.getLogger(FillapixBoard.class.getName());

    private int width;
    private int height;

    public FillapixBoard(int width, int height)
    {
        super(width, height);
        this.width = width;
        this.height = height;
    }

    public FillapixBoard(int size)
    {
        this(size, size);
    }

    public FillapixCell getCell(int x, int y)
    {
        if(x >= width || y >= height)
        {
            LOGGER.log(Level.SEVERE, "Fillapix: Attempting to access an out of bounds cell: " + ", x: " + x + ", y: " + y);
            return null;
        }
        else
        {
            return (FillapixCell) super.getCell(x, y);
        }
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public FillapixBoard copy()
    {
        FillapixBoard newGridBoard = new FillapixBoard(width, height);
        for(int x = 0; x < this.width; x++)
        {
            for(int y = 0; y < this.height; y++)
            {
                newGridBoard.setCell(x, y, getCell(x, y).copy());
            }
        }
        return newGridBoard;
    }

    /**
     * Finds the number of cells that match the specified state around and on a particular cell
     *
     * @param cell  the cell we're looking around
     * @param state the CellState whether it's black, white, or unknown
     *
     * @return integer number of cells that match specified state
     */
    public int getNumCells(FillapixCell cell, int state)
    {
        int numCells = 0;
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                int x = cell.getLocation().x + i;
                int y = cell.getLocation().y + j;
                if(x > -1 && x < width && y > -1 && y < height)
                {
                    if((getCell(x, y).isUnknown() && state == FillapixCell.UNKNOWN) || (getCell(x, y).isBlack() && state == FillapixCell.BLACK) || (getCell(x, y).isWhite() && state == FillapixCell.WHITE))
                    {
                        numCells++;
                    }
                }
            }
        }
        return numCells;
    }
}
