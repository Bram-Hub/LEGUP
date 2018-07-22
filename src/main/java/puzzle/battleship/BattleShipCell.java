package puzzle.battleship;

import model.gameboard.GridCell;

import java.awt.*;

public class BattleShipCell extends GridCell {
    private boolean isShipSegment;

    public BattleShipCell(int valueInt, Point location) {
        super(valueInt, location);
        this.isShipSegment =false;
    }

    @Override
    public Integer getData()
    {
        return (Integer)data;
    }

    public BattleShipCellType getType() { // FINISH IMPLEMENTING
        switch ((Integer) data) {
            // case
            // return BattleShipCellType._
        }
        return null;
    }

    public boolean isShipSegment() { return isShipSegment; }

    public void setShipSegment(boolean isShipSegment) { this.isShipSegment = isShipSegment; }
}
