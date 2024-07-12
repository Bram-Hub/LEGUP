package edu.rpi.legup.puzzle.sudoku.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class UnknownTile extends PlaceableElement {
    public UnknownTile() {
        super(
                "SUDO-UNPL-0001",
                "Unknown Tile",
                "A blank tile",
                "edu/rpi/legup/images/sudoku/tiles/UnknownTile.png");
    }
}