package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class EmptyTile extends PlaceableElement {

    public EmptyTile() {
        super(
                "MINE-PLAC-0002",
                "Empty",
                "An empty tile",
                "edu/rpi/legup/images/minesweeper/tiles/Empty.png");
    }
}
