package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.Point;

public class BinaryCell extends GridCell<Integer> {
    public BinaryCell(int valueInt, Point location) {
        super(valueInt, location);
    }

    public BinaryType getType() {
        switch (data) {
            case 0:
                return BinaryType.ZERO;
            case 1:
                return BinaryType.ONE;
            case 2:
                return BinaryType.UNKNOWN;
            default:
                if (data > 1) {
                    return BinaryType.UNKNOWN;
                }
        }
        return null;
    }

    @Override
    public BinaryCell copy() {
        BinaryCell copy = new BinaryCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}