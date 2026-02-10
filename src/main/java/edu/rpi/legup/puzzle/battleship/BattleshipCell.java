package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;

import java.awt.*;

public class BattleshipCell extends GridCell<BattleshipType> {

    /**
     * BattleShipCell Constructor - creates a BattleShipCell from the specified value and location
     *
     * @param value value of the BattleShipCell
     * @param location position of the BattleShipCell
     */
    public BattleshipCell(BattleshipType value, Point location) {
        super(value, location);
    }

    /**
     * Gets the type of this BattleShipCell
     *
     * @return type of BattleShipCell
     */
    public BattleshipType getType() {
        return data;
    }

    /**
     * Performs a deep copy on the BattleShipCell
     *
     * @return a new copy of the BattleShipCell that is independent of this one
     */
    public BattleshipCell copy() {
        BattleshipCell copy = new BattleshipCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    @Override
    public boolean isKnown() {return !(data == BattleshipType.UNKNOWN);}
}
