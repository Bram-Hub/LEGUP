package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.GridBoard;

public class YinYangBoard extends GridBoard {

    public YinYangBoard(int width, int height) {
        super(width, height);
    }

    /**
     * Retrieves the cell at the specified (x, y) location as a YinYangCell.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the YinYangCell at (x, y), or null if out of bounds
     */
    @Override
    public YinYangCell getCell(int x, int y) {
        return (YinYangCell) super.getCell(x, y);
    }

    /**
     * Converts the board into a 2D array of YinYangType values.
     *
     * @return a 2D array of YinYangType values representing the board state
     */
    public YinYangType[][] getTypeArray() {
        YinYangType[][] types = new YinYangType[dimension.height][dimension.width];
        for (int y = 0; y < dimension.height; y++) {
            for (int x = 0; x < dimension.width; x++) {
                YinYangCell cell = getCell(x, y);
                types[y][x] = (cell != null) ? cell.getType() : YinYangType.UNKNOWN;
            }
        }
        return types;
    }

    @Override
    public YinYangBoard copy() {
        YinYangBoard copy = new YinYangBoard(dimension.width, dimension.height);
        for (int y = 0; y < this.dimension.height; y++) {
            for (int x = 0; x < this.dimension.width; x++) {
                YinYangCell originalCell = getCell(x, y);
                if (originalCell != null) {
                    copy.setCell(x, y, originalCell.copy());
                }
            }
        }
        for (var e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}