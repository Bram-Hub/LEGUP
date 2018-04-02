package puzzle.battleship;

import model.gameboard.GridCell;

import java.awt.*;

public class BattleShipCell extends GridCell {
    private boolean isShipSegment;

    public BattleShipCell(int valueInt, Point location) {
        super(valueInt, location);
        this.isShipSegment =false;
    }

    public BattleShipCellType getType() { // FINISH IMPLEMENTING
        switch (valueInt) {
            // case
            // return BattleShipCellType._
        }
        return null;
    }

    public boolean isShipSegment() { return isShipSegment; }

    public void setShipSegment(boolean isShipSegment) { this.isShipSegment = isShipSegment; }
}
