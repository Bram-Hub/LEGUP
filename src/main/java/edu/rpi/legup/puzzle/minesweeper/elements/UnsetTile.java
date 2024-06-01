package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class UnsetTile extends NonPlaceableElement {

    public UnsetTile() {
        super(
                "MINE-UNPL-0002",
                "Unset",
                "An unset tile",
                "edu/rpi/legup/images/minesweeper/tiles/Unset.png");
    }
}
