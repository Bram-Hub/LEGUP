package puzzles.sudoku;

import model.gameboard.GridBoard;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SudokuBoard extends GridBoard
{
    private final static Logger LOGGER = Logger.getLogger(SudokuBoard.class.getName());

    private int groupWidth;
    private int groupHeight;

    public SudokuBoard(int width, int height)
    {
        super(width, height);
        groupWidth = (int)Math.sqrt(width);
        groupHeight = (int)Math.sqrt(height);
    }

    /**
     * Gets the SudokuCell in the specified group index at the x and y location given
     * The group index must be by less than the width (or height) of the board and the
     * x and y location is relative to the group. This means the x and y values must be
     * less the square root of the width (or height) of the board.
     *
     * @param groupIndex group index of the cell
     * @param x x location relative to the group
     * @param y y location relative to the group
     * @return cell in the specified group index at the given x and y location
     */
    public SudokuCell getCell(int groupIndex, int x, int y)
    {
        if(groupIndex >= width || x >= groupWidth || y >= groupHeight)
        {
            LOGGER.log(Level.SEVERE, "Sudoku: Attempting to access an out of bounds cell: Group Index: " +
                    groupIndex + ", x: " + x + ", y: " + y);
            return null;
        }
        else
        {
            return (SudokuCell) getCell(x + (groupIndex % groupWidth) * groupWidth, y + (groupIndex / groupHeight) * groupHeight);
        }
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public SudokuBoard copy()
    {
        SudokuBoard newGridBoard = new SudokuBoard(this.width, this.height);
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
