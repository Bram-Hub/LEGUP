package edu.rpi.legup.puzzle.minesweeper.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class BombTile extends PlaceableElement {
    public BombTile() {
        super(
                "MINE-UNPL-0001",
                "Bomb",
                "A bomb",
                "edu/rpi/legup/images/minesweeper/tiles/Bomb.png");
    }
}
