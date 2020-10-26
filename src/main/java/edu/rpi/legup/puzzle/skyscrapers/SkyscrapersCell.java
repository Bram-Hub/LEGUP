package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class SkyscrapersCell extends GridCell<Integer> {
	private int max;

    public SkyscrapersCell(int valueInt, Point location, int size) {
        super(valueInt, location);
        this.max = size;
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
