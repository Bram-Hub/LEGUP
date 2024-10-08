package edu.rpi.legup.puzzle.sudoku.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class NumberTile extends PlaceableElement {
    private int object_num;

    public NumberTile() {
        super("SUDO-PLAC-0001", "Number Tile", "A numbered tile", null);
        object_num = 0;
    }
}
