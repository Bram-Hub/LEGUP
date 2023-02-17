package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class SkyscrapersCell extends GridCell<SkyscrapersType> {
    private int max;

    public SkyscrapersCell(SkyscrapersType value, Point location, int size) {
        super(value, location);
        this.max = size;
    }

    public SkyscrapersType getType() {
        switch (data) {
            case UNKNOWN:
                return SkyscrapersType.UNKNOWN;
            default:
                return SkyscrapersType.Number;
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
