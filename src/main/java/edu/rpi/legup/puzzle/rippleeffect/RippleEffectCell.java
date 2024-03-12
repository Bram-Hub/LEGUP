package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;

public class RippleEffectCell extends GridCell<Integer> {
    private int number;

    public RippleEffectCell(int type, Point location, int number) {
        super(type, location);
        this.number = number;
    }

    public RippleEffectCellType getType() {
        switch (getData()) {
            case 1:
                return RippleEffectCellType.WHITE;
            case 2:
                return RippleEffectCellType.BLUE;
            case 3:
                return RippleEffectCellType.RED;
            case 4:
                return RippleEffectCellType.YELLOW;
            case 5:
                return RippleEffectCellType.GREEN;
            default:
                return null;
        }
    }

    public int getNumber(){
        return number;
    }
}