package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class BinaryCell extends GridCell<Integer> {
    public BinaryCell(int valueInt, Point location) {
        super(valueInt, location);
    }

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

    @Override
    public BinaryCell copy() {
        BinaryCell copy = new BinaryCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
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
                if (this.data == 0) {
                    this.data = 1;
                }
                else {
                    this.data = 0;
                }
            }
            else {
                if (m.getButton() == MouseEvent.BUTTON3) {
                    if (this.data == 0) {
                        this.data = 1;
                    }
                    else {
                        this.data = 0;
                    }
                }
            }
        }
        else { // unknown tile
            this.data = 2;
        }
    }
}
