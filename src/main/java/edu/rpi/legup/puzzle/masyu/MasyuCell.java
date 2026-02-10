package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;

public class MasyuCell extends GridCell<MasyuType> {

    public MasyuCell(MasyuType value, Point location) {
        super(value, location);
    }

    public MasyuType getType() {
        return data;
    }

    @Override
    public MasyuCell copy() {
        MasyuCell copy = new MasyuCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return !(data == MasyuType.UNKNOWN);}

}
