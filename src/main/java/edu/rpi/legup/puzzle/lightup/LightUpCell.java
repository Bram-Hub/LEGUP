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

    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementID()) {
            case "LTUP-PLAC-0001":
                this.data = -4;
                break;
            case "LTUP-UNPL-0002":
                this.data = -1;
                break;
            case "LTUP-UNPL-0003":
                this.data = -2;
                break;
        }
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
