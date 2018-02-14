package model.gameboard;

public class GridBoard extends Board
{
    private GridCell[][] gridCells;
    private int width;
    private int height;

    /**
     * GridBoard Constructor - models a GridBoard
     *
     * @param width width of the GridBoard
     * @param height height of the GridBoard
     */
    public GridBoard(int width, int height)
    {
        gridCells = new GridCell[width][height];
        this.width = width;
        this.height = height;
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
        return gridCells[x][y];
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
        gridCells[x][y] = cell;
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