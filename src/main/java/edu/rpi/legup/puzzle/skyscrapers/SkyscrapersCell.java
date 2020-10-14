package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class SkyscrapersCell extends GridCell<Integer> {

    public SkyscrapersCell(int valueInt, Point location) {
        super(valueInt, location);
    }

    public SkyscrapersType getType() {
        switch (data) {
            case 0:
                return SkyscrapersType.UNKNOWN;
            case 1:
                return SkyscrapersType.TREE;
            case 2:
                return SkyscrapersType.GRASS;
            case 3:
                return SkyscrapersType.TENT;
            default:
                return null;
        }
    }

    @Override
    public SkyscrapersCell copy() {
    	SkyscrapersCell copy = new SkyscrapersCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
