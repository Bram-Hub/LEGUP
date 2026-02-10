package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;

public class NurikabeCell extends GridCell<Integer> {

    /**
     * NurikabeCell Constructor - creates a NurikabeCell from the specified value and location
     *
     * @param value value of the NurikabeCell
     * @param location position of the NurikabeCell
     */
    public NurikabeCell(int value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this NurikabeCell
     *
     * @return type of NurikabeCell
     */
    public NurikabeType getType() {
        switch (data) {
            case -2:
                return NurikabeType.UNKNOWN;
            case -1:
                return NurikabeType.BLACK;
            case 0:
                return NurikabeType.WHITE;
            default:
                if (data > 0) {
                    return NurikabeType.NUMBER;
                }
        }
        return null;
    }

    /**
     * Sets the type of this NurikabeCell
     *
     * @param e element to set the type of this nurikabe cell to
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementID()) {
            case "NURI-ELEM-0001":
                this.data = -1;
                break;
            case "NURI-ELEM-0004":
                this.data = 0;
                break;
            case "NURI-ELEM-0002":
                switch (m.getButton()) {
                    case MouseEvent.BUTTON1:
                        if (this.data <= 0 || this.data > 8) {
                            this.data = 1;
                        } else {
                            this.data = this.data + 1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if (this.data > 1) {
                            this.data = this.data - 1;
                        } else {
                            this.data = 9;
                        }
                        break;
                }
                break;
            default:
                this.data = -2;
                break;
        }
    }

    /**
     * Performs a deep copy on the NurikabeCell
     *
     * @return a new copy of the NurikabeCell that is independent of this one
     */
    @Override
    public NurikabeCell copy() {
        NurikabeCell copy = new NurikabeCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return data != -2;}

    @Override
    public String describeState() {
        return getType().toString().toLowerCase();
    }
}
