package edu.rpi.legup.puzzle.lightup.elements;

import edu.rpi.legup.model.elements.PlaceableElement;

public class NumberTile extends PlaceableElement {

    int object_number;

    // Follow the default format and resolves the NoSuchMethod error
    public NumberTile() {
        super(
                "LTUP-ELEM-0003",
                "Number Tile",
                "The number tile",
                "edu/rpi/legup/images/lightup/1.gif");
    }

    public NumberTile(int num) {
        super(
                "LTUP-ELEM-0003",
                "Number Tile",
                "The number tile",
                "edu/rpi/legup/images/lightup/" + num + ".gif");
        if (num > 3 || num < 1) num = 1;
        object_number = num;
    }
}
