package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;

public class ThermometerCell extends GridCell<Integer> {

    // information about the cell needed to display it
    private ThermometerType type;
    private ThermometerFill fill;
    private int rotation;

    /**
     * Constructs a ThermometerCell with the specified location, type, fill, and rotation.
     *
     * @param location the position of the cell
     * @param t the ThermometerType of the cell
     * @param f the ThermometerFill state of the cell
     * @param r the rotation value representing orientation or numeric data
     */
    public ThermometerCell(Point location, ThermometerType t, ThermometerFill f, int r) {
        // since we do not use get/set data value int can be any value
        super(1, location);
        type = t;
        fill = f;
        rotation = r;
    }

    /**
     * Sets the type of this ThermometerCell.
     *
     * @param t the ThermometerType to assign
     */
    public void setType(ThermometerType t) {
        type = t;
    }

    /**
     * Gets the type of this ThermometerCell.
     *
     * @return the ThermometerType of the cell
     */
    public ThermometerType getType() {
        return type;
    }

    /**
     * Sets the fill state of this ThermometerCell.
     *
     * @param f the ThermometerFill to assign
     */
    public void setFill(ThermometerFill f) {
        fill = f;
    }

    /**
     * Gets the fill state of this ThermometerCell.
     *
     * @return the ThermometerFill of the cell
     */
    public ThermometerFill getFill() {
        return fill;
    }

    /**
     * Sets the rotation value of this ThermometerCell.
     *
     * @param r the rotation value
     */
    public void setRotation(int r) {
        rotation = r;
    }

    /**
     * Gets the rotation value of this ThermometerCell.
     *
     * @return the rotation value
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Creates a deep copy of this ThermometerCell.
     *
     * @return a new ThermometerCell with the same properties
     */
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

    /**
     * Returns a string representation of this ThermometerCell.
     *
     * @return a string containing the location, type, and fill of the cell
     */
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
    public boolean isKnown() {
        return !(data == 0);
    }

    @Override
    public String describeState(boolean isPlural) {
        return getFill().toString().toLowerCase();
    }
}
