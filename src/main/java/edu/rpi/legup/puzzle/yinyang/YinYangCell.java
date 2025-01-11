package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.GridCell;

public class YinYangCell extends GridCell<YinYangType> {

    public YinYangCell(YinYangType type, int x, int y) {
        super(type, x, y); // Use YinYangType directly
    }

    /**
     * Gets the type of the cell.
     *
     * @return the YinYangType of the cell
     */
    public YinYangType getType() {
        return getData();
    }

    /**
     * Sets the type of the cell.
     *
     * @param type the YinYangType to set
     */
    public void setType(YinYangType type) {
        setData(type);
    }

    /**
     * Gets the X coordinate of the cell.
     *
     * @return the X coordinate
     */
    public int getX() {
        return location.x;
    }

    /**
     * Gets the Y coordinate of the cell.
     *
     * @return the Y coordinate
     */
    public int getY() {
        return location.y;
    }

    /**
     * Performs a deep copy of the YinYangCell.
     *
     * @return a new copy of the YinYangCell
     */
    @Override
    public YinYangCell copy() {
        return new YinYangCell(getType(), location.x, location.y);
    }
}
