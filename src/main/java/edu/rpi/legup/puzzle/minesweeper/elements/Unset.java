package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class Unset extends NonPlaceableElement {
    public Unset() {
        super(
                "MINE-UNPL-0002",
                "Unset",
                "A blank tile",
                "edu/rpi/legup/images/fillapix/tiles/UnknownTile.png");
    }
}
