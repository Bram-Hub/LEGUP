package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RippleEffectCell extends GridCell<Integer>{
    
    public RippleEffectCell(int value, Point location) {
        super(value, location);
    }

    public void incrementCell() {
        int curr = getData();
        setData(curr++);
    }

    public void decrementCell() {
        int curr = getData();
        setData(curr--);
    }
}
