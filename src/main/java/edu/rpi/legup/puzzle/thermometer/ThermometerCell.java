package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.Point;

public class ThermometerCell extends GridCell<Integer> {
    public ThermometerCell(int valueInt, Point location) {
        super(valueInt, location);
    }

    public ThermometerType getType() {
        switch (data) {
            case 0:
                return ThermometerType.UNKNOWN;
            case 1:
                return ThermometerType.HEAD;
            case 2:
                return ThermometerType.SHAFT;
            case 3:
                return ThermometerType.TIP;
        }
        return null;
    }

    public ThermometerFill getFill() {
        switch (data) {
            case 0:
                return ThermometerFill.UNKNOWN;
            case 1:
                return ThermometerFill.FILLED;
            case 2:
                return ThermometerFill.EMPTY;
            case 3:
                return ThermometerFill.BLOCKED;
        }
        return null;
    }

    @Override
    public ThermometerCell copy() {
        ThermometerCell copy = new ThermometerCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
