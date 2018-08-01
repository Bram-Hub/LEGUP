package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class BattleShipCell extends GridCell<Integer>
{

    public BattleShipCell(int valueInt, Point location) {
        super(valueInt, location);
    }

    public BattleShipCellType getType() {
        return BattleShipCellType.getType(getData());
    }


    public BattleShipCell copy()
    {
        BattleShipCell copy = new BattleShipCell(data, (Point)location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }

}
