package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class MineTile extends NonPlaceableElement {
    public MineTile() {
        super(
                "MINE-UNPL-0001",
                "Mine",
                "A mine",
                "edu/rpi/legup/images/minesweeper/tiles/mine.png");
    }
}
