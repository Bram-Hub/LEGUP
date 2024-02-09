package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.Point;

public class RippleEffectCell extends GridCell<RippleEffectCellType> {
    public RippleEffectCell(RippleEffectCellType type, Point location) {
        super(type, location);
    }
}
