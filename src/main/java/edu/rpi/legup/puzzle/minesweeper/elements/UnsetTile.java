package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class UnsetTile extends PlaceableElement {

    public UnsetTile() {
        super(
                "MINE-UNPL-0000",
                "Unset",
                "An unset tile",
                "edu/rpi/legup/images/minesweeper/tiles/Unset.png");
    }
}
