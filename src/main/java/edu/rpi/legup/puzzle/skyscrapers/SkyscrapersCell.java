package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.awt.event.MouseEvent;

import static edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType.convertToSkyType;

public class SkyscrapersCell extends GridCell<Integer> {
    private int max;

    public SkyscrapersCell(Integer value, Point location, int size) {
        super(value, location);
        this.max = size;
    }

    public SkyscrapersType getType() {
        switch (convertToSkyType(data)){
            case UNKNOWN:
                return SkyscrapersType.UNKNOWN;
            default:
                return SkyscrapersType.Number;
        }
    }

    @Override
    public void setType(Element e, MouseEvent m) {
        switch (e.getElementID()){
            case "SKYS-UNPL-0001":
                this.data = 0;
                break;
            case "SKYS-UNPL-0002":
                switch (m.getButton()){
                    case MouseEvent.BUTTON1:
                        if (this.data <= 0 || this.data >= this.max) {
                            this.data = 1;
                        }
                        else {
                            this.data = this.data + 1;
                        }
                        break;
                    case MouseEvent.BUTTON3:
                        if (this.data > 1) {
                            this.data = this.data - 1;
                        }
                        else {
                            this.data = this.max;
                        }
                        break;
                }
                break;
        }
    }


    public int getMax() {
        return max;
    }

    @Override
    public SkyscrapersCell copy() {
        SkyscrapersCell copy = new SkyscrapersCell(data, (Point) location.clone(), max);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
