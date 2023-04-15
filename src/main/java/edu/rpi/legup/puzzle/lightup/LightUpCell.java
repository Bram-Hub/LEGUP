package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.awt.event.MouseEvent;

public class LightUpCell extends GridCell<Integer> {
    private boolean isLite;

    public LightUpCell(int valueInt, Point location) {
        super(valueInt, location);
        this.isLite = false;
    }

    public LightUpCellType getType() {
        switch (data) {
            case -4:
                return LightUpCellType.BULB;
            case -3:
                return LightUpCellType.EMPTY;
            case -2:
                return LightUpCellType.UNKNOWN;
            case -1:
                return LightUpCellType.BLACK;
            default:
                if (data >= 0) {
                    return LightUpCellType.NUMBER;
                }
        }
        return null;
    }

    /**
     * Sets the type of this LightUpCell
     *
     * @param e element to set the type of this LightUp cell to
     * @param m mouse event being processed
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        switch(e.getElementName()) {
            case "Black Tile":
                this.data = -1;
                break;
            case "Bulb Tile":
                this.data = -4;
                break;
            case "Number Tile":
                if (m.getButton() == MouseEvent.BUTTON1) {
                    // Place a number tile if not a number tile
                    if (this.data < 0 || this.data >= 9) {
                        this.data = 1;
                    }
                    else {
                        // Increase number
                        this.data++;
                    }
                }
                else {
                    if (m.getButton() == MouseEvent.BUTTON3) {
                        // Decrease number value (down to 1) if pressing number tile
                        if (this.data > 1 && this.data <= 8) {
                            this.data--;
                        }
                        else {
                            this.data = 1;
                        }
                    }
                }
                break;
            default:
                this.data = -3;
                break;
        }
    }

    public boolean isLite() {
        return isLite;
    }

    public void setLite(boolean isLite) {
        this.isLite = isLite;
    }

    @Override
    public LightUpCell copy() {
        LightUpCell copy = new LightUpCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
