package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;

public class ThermometerCell extends GridCell<Integer> {

    // information about the cell needed to display it
    private ThermometerType type;
    private ThermometerFill fill;
    private int rotation;

    public ThermometerCell(Point location, ThermometerType t, ThermometerFill f, int r) {
        // since we do not use get/set data value int can be any value
        super(1, location);
        type = t;
        fill = f;
        rotation = r;
    }

    // Note: setdata does not work for our purposes
    public void setType(ThermometerType t) {
        type = t;
    }

    public ThermometerType getType() {
        return type;
    }

    public void setFill(ThermometerFill f) {
        fill = f;
    }

    public ThermometerFill getFill() {
        return fill;
    }

    public void setRotation(int r) {
        rotation = r;
    }

    public int getRotation() {
        return rotation;
    }

    @Override
    public ThermometerCell copy() {
        ThermometerCell copy =
                new ThermometerCell((Point) location.clone(), this.type, this.fill, this.rotation);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public String toString() {
        return "("
                + location.getX()
                + ", "
                + location.getY()
                + ") TYPE = "
                + getType()
                + " FILL = "
                + getFill();
    }

    @Override
    public boolean isKnown() {return !(data == 0);}
}
