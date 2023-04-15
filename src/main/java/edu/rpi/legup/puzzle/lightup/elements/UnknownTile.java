package edu.rpi.legup.puzzle.lightup.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class UnknownTile extends NonPlaceableElement {
    public UnknownTile() {
		// This is the same tile as found in Nurikabe, but under LightUp to make it appear in the panel
        super("NURI-UNPL-0002", "Unknown Tile", "A blank tile", "edu/rpi/legup/images/nurikabe/tiles/UnknownTile.png");
    }
}
