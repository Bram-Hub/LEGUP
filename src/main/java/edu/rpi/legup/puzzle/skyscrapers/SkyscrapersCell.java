package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

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
