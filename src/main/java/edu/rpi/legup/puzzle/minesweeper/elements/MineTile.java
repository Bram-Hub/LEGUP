package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class MineTile extends PlaceableElement {
    public MineTile() {
        super(
                "MINE-UNPL-0001",
                "Mine",
                "A mine",
                "edu/rpi/legup/images/minesweeper/tiles/Mine.png");
    }
}
