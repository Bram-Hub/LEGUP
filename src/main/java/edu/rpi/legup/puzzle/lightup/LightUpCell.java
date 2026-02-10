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
            case "LTUP-ELEM-0002":
                this.data = -4;
                break;
            case "LTUP-ELEM-0001":
                this.data = -1;
                break;
            case "LTUP-ELEM-0004":
                this.data = -2;
                break;
            case "LTUP-ELEM-0003":
                switch (m.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (this.data < 0 || this.data > 3) {
                            this.data = 0;
                        } else {
                            this.data = this.data + 1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if (this.data > 0) {
                            this.data = this.data - 1;
                        } else {
                            this.data = 4;
                        }
                        break;
                }
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
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return data != -2;}
}
