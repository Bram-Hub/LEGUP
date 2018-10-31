package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.logging.Logger;

public class FillapixBoard extends GridBoard {
    private final static Logger LOGGER = Logger.getLogger(FillapixBoard.class.getName());

    public FillapixBoard(int width, int height) {
        super(width, height);
    }

    public FillapixBoard(int size) {
        this(size, size);
    }

    public FillapixCell getCell(int x, int y) {
        return (FillapixCell) super.getCell(x, y);
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public FillapixBoard copy() {
        FillapixBoard copy = new FillapixBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }

    /**
     * Finds the number of cells that match the specified type around and on a particular cell
     *
     * @param cell the cell we're looking around
     * @param type the CellState whether it's black, white, or unknown
     * @return integer number of cells that match specified type
     */
    public int getNumCells(FillapixCell cell, FillapixCellType type) {
        Point loc = cell.getLocation();

        int numCells = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                FillapixCell c = getCell(loc.x + i, loc.y + j);
                if (c != null && c.getType() == type) {
                    numCells++;
                }
            }
        }

        return numCells;
    }
}
