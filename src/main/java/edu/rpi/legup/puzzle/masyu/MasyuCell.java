package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class MasyuCell extends GridCell<MasyuType> {

    public MasyuCell(int value, Point location) {
        super(value, location);
    }

    public MasyuType getType() {
        switch (data) {
            case 0:
                return MasyuType.UNKNOWN;
            case 1:
                return MasyuType.BLACK;
            case 2:
                return MasyuType.WHITE;
            case 3:
                return MasyuType.LINE;
            default:
                return null;
        }
    }

    @Override
    public MasyuCell copy() {
        MasyuCell copy = new MasyuCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
