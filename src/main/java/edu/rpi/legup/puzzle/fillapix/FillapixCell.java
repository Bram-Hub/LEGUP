package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class FillapixCell extends GridCell<Integer> implements Comparable<FillapixCell> {

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

    public void setCellType(FillapixCellType type) {
        data = type.value * 100 + (data % 100);
    }

    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementID()) {
            case "FPIX-ELEM-0001":
                this.setCellType(FillapixCellType.BLACK);
                break;
            case "FPIX-ELEM-0004":
                this.setCellType(FillapixCellType.WHITE);
                break;
            case "FPIX-ELEM-0002":
                int n = this.getNumber();
                switch (m.getButton()) {
                    case MouseEvent.BUTTON1:
                        n++;
                        break;
                    case MouseEvent.BUTTON3:
                        n--;
                        break;
                }
                if (n > 9) {
                    n = 0;
                }
                if (n < 0) {
                    n = 9;
                }
                this.setNumber(n);
                break;
            default:
                this.setCellType(FillapixCellType.UNKNOWN);
                this.data = -1;
                break;
        }
    }

    @Override
    public boolean isKnown() {return data != 0;}

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
        cell.setGoal(isGoal);
        return cell;
    }

    public boolean equals(FillapixCell otherCell) {
        //        return this.location.equals(otherCell.location) && this.index == otherCell.index
        // &&
        // this.data == otherCell.data;
        // return this.index == otherCell.index && this.data == otherCell.data;
        // return this.index == otherCell.index;
        return this.location.x == otherCell.location.x && this.location.y == otherCell.location.y;
    }

    public int compareTo(FillapixCell otherCell) {
        return this.index - otherCell.index;
    }

    public int hashCode() {
        return Objects.hash(this.index);
    }
}
