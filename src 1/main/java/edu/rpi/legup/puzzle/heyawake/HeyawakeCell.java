package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class HeyawakeCell extends GridCell<Integer> {

    private int regionIndex;

    public HeyawakeCell(int valueInt, Point location, int regionIndex) {
        super(valueInt, location);
        this.regionIndex = regionIndex;
    }

    public int getRegionIndex() {
        return this.regionIndex;
    }

    @Override
    public HeyawakeCell copy() {
        HeyawakeCell copy = new HeyawakeCell(data, (Point) location.clone(), regionIndex);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
