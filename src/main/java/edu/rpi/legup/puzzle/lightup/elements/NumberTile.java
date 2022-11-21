package edu.rpi.legup.puzzle.lightup.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class NumberTile extends NonPlaceableElement {

    int object_number;
    public NumberTile(){
        super("LTUP-UNPL-0001", "Number Tile", "The number tile", "edu/rpi/legup/images/lightup/" + "0.gif");
    }//default consturctor
    
    public NumberTile(int num) {
        super("LTUP-UNPL-0001", "Number Tile", "The number tile", "edu/rpi/legup/images/lightup/" + num + ".gif");
        if (num > 3 || num < 1) num = 1;
        object_number = num;
    }
}
