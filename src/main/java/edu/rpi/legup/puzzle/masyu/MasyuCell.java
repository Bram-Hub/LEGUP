package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class MasyuCell extends GridCell<MasyuType> {

    public MasyuCell(MasyuType value, Point location) {
        super(value, location);
    }

    public MasyuType getType() {
        switch (data) {
            case UNKNOWN:
                return MasyuType.UNKNOWN;
            case BLACK:
                return MasyuType.BLACK;
            case WHITE:
                return MasyuType.WHITE;
            case LINE:
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
