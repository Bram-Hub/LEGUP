package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.Point;

public class ThermometerCell extends GridCell<Integer> {
    private ThermometerType type;
    private ThermometerFill fill;
    public ThermometerCell(int valueInt, Point location) {
        super(valueInt, location);
        type = ThermometerType.UNKNOWN;
        fill = ThermometerFill.UNKNOWN;
    }




    //Note: setdata does not work for our purposes
    public void setType(ThermometerType t){
        type = t;
    }


    public ThermometerType getType() {
        return switch (type.ordinal()) {
            case 0 -> ThermometerType.UNKNOWN;
            case 1 -> ThermometerType.HEAD;
            case 2 -> ThermometerType.SHAFT;
            case 3 -> ThermometerType.TIP;
            default -> null;
        };
    }

    public void setFill(ThermometerFill f){
        fill = f;
    }

    public ThermometerFill getFill() {
        return switch (fill.ordinal()) {
            case 0 -> ThermometerFill.UNKNOWN;
            case 1 -> ThermometerFill.FILLED;
            case 2 -> ThermometerFill.EMPTY;
            case 3 -> ThermometerFill.BLOCKED;
            default -> null;
        };
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
