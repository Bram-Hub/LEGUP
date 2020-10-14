package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;

public class SkyscrapersCell extends GridCell<Integer> {
    private int val;
    private boolean isEmpty;

    public SkyscrapersCell(int valueInt, Point location) {
        super(valueInt, location);
        this.val = valueInt;
        this.isEmpty = true;
    }

    //might add a new switch case for visible buildings
    //i'm not sure how it'll be implemented yet though (part of grid or not)
    public SkyscrapersCell getType() {
        switch (data) {
            case 0:
               return SkyscrapersCellType.EMPTY;
            default:
              if (data >= 0) {
                this.isEmpty = false;
                return SkyscrapersCellType.NUMBER;
              }
        }
        return null;
    }

    public boolean isCellEmpty() {
        return this.isEmpty;
    }

    public void setValue(int val) {
        this.value = val;
    }

    @Override
    public SkyscrapersCell copy() {
        SkyscrapersCell copy = new SkyscrapersCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
