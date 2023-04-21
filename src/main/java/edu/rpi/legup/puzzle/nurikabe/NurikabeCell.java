package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.elements.Element;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NurikabeCell extends GridCell<Integer> {

    /**
     * NurikabeCell Constructor - creates a NurikabeCell from the specified value and location
     *
     * @param value    value of the NurikabeCell
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
        if (e.getElementID().equals("NURI-PLAC-0001")) {
            this.data = -1;
        }
        else {
            if (e.getElementID().equals("NURI-PLAC-0002")) {
                this.data = 0;
            }
            else {
                if (e.getElementID().equals("NURI-UNPL-0001")) {
                    if (m.getButton() == MouseEvent.BUTTON1) {
                        if (this.data <= 0 || this.data > 8) {
                            this.data = 1;
                        }
                        else {
                            this.data = this.data + 1;
                        }
                    }
                    else {
                        if (m.getButton() == MouseEvent.BUTTON3) {
                            if (this.data > 1) {
                                this.data = this.data - 1;
                            }
                            else {
                                this.data = 9;
                            }
                        }
                    }
                }
                else { // unknown tile
                    this.data = -2;
                }
            }
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
        return copy;
    }
}
