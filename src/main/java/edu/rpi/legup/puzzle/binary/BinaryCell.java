package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class BinaryCell extends GridCell<Integer> {
    /**
     * BinaryCell Constructor - creates a BinaryCell from the specified value and location
     *
     * @param value value of the BinaryCell
     * @param location position of the BinaryCell
     */
    public BinaryCell(int value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this BinaryCell
     *
     * @return type of BinaryCell
     */
    public BinaryType getType() {
        switch (data) {
            case 0:
                return BinaryType.ZERO;
            case 1:
                return BinaryType.ONE;
            case 2:
                return BinaryType.UNKNOWN;
            default:
                if (data > 1) {
                    return BinaryType.UNKNOWN;
                }
        }
        return null;
    }

    /**
     * Performs a deep copy on the BinaryCell
     *
     * @return a new copy of the BinaryCell that is independent of this one
     */
    @Override
    public BinaryCell copy() {
        BinaryCell copy = new BinaryCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    /**
     * Sets the type of this BinaryCell
     *
     * @param e element to set the type of this binary cell to
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        if (e.getElementName().equals("Number Tile")) {
            if (m.getButton() == MouseEvent.BUTTON1) {
                if (this.data == 2) {
                    this.data = 0;
                } else {
                    this.data = this.data + 1;
                }
            } else {
                if (m.getButton() == MouseEvent.BUTTON3) {
                    if (this.data > 0) {
                        this.data = this.data - 1;
                    } else {
                        this.data = 2;
                    }
                }
            }
        } else { // unknown tile
            this.data = 2;
        }
    }

    @Override
    public boolean isKnown() {return !(data == 2);}

    @Override
    public String describeState(boolean isPlural) {
        return getType().toString().toLowerCase();
    }
}
