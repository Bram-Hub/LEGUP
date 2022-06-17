package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class BattleShipCell extends GridCell<BattleShipType> {

    /**
     * BattleShipCell Constructor - creates a BattleShipCell from the specified value and location
     *
     * @param value    value of the BattleShipCell
     * @param location position of the BattleShipCell
     */
    public BattleShipCell(BattleShipType value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this BattleShipCell
     *
     * @return type of BattleShipCell
     */
    public BattleShipType getType() {
        return data;
    }

    /**
     * Performs a deep copy on the BattleShipCell
     *
     * @return a new copy of the BattleShipCell that is independent of this one
     */
    public BattleShipCell copy() {
        BattleShipCell copy = new BattleShipCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
