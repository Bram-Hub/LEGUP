package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class NurikabeCell extends GridCell<Integer> {

/**
     * NurikabeCell Constructor - creates a NurikabeCell from the specified value and location
     *
     * @param value    value of the NurikabeCell
     * @param location position of the NurikabeCell
     */
    public NurikabeCell(int value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this NurikabeCell
     *
     * @return type of NurikabeCell
     */
    public NurikabeType getType() {
        switch (data) {
            case -2:
                return NurikabeType.UNKNOWN;
            case -1:
                return NurikabeType.BLACK;
            case 0:
                return NurikabeType.WHITE;
            default:
                if (data > 0) {
                    return NurikabeType.NUMBER;
                }
        }
        return null;
    }

    /**
     * Performs a deep copy on the NurikabeCell
     *
     * @return a new copy of the NurikabeCell that is independent of this one
     */
    @Override
    public NurikabeCell copy() {
        NurikabeCell copy = new NurikabeCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
