package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class FillapixCell extends GridCell<Integer> {

    public static final int DEFAULT_VALUE = 10;

    public FillapixCell(int value, Point location) {
        super(value, location);
    }

    public int getNumber() {
        int temp = (data % 100);
        return temp == 10 ? -1 : temp;
    }

    public void setNumber(int number) {
        int temp = number == -1 ? 10 : number;
        data = (data / 100) * 100 + temp;
    }

    public FillapixCellType getType() {
        switch (data / 100) {
            case 0:
                return FillapixCellType.UNKNOWN;
            case 1:
                return FillapixCellType.BLACK;
            default:
                return FillapixCellType.WHITE;
        }
    }

    public void setType(FillapixCellType type) {
        data = type.value * 100 + (data % 100);
    }

    /**
     * Performs a deep copy on the FillapixCell
     *
     * @return a new copy of the FillapixCell that is independent of this one
     */
    @Override
    public FillapixCell copy() {
        FillapixCell cell = new FillapixCell(data, (Point) location.clone());
        cell.setIndex(index);
        cell.setModifiable(isModifiable);
        return cell;
    }
}