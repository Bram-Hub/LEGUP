package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.puzzle.rippleeffect.RippleEffectCell;

import java.util.ArrayList;
import java.util.List;

public class RippleEffectBoard extends GridBoard{
    private List regions;

    public RippleEffectBoard(int width, int height) {
        super(width, height);
        regions = new ArrayList<RippleEffectRegion>();
    }

    public RippleEffectBoard(int size) {
        this(size, size);
    }

    @Override
    public RippleEffectCell getCell(int x, int y) {
        return (RippleEffectCell) super.getCell(x, y);
    }

    @Override
    public RippleEffectBoard copy() {
        // temporary
        return null;
    }
}
