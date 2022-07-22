package edu.rpi.legup.puzzle.nurikabe.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class NumberTile extends NonPlaceableElement {
    private int object_num;
    public NumberTile( int num ) {
        super("NURI-UNPL-0001", "Number Tile", "A numbered tile", "edu/rpi/legup/images/nurikabe/rules/BetweenRegions.png");
        object_num = num;
    }

    /**
    *   @return this object's tile number...
    * */
    public int getTileNumber()
    {return object_num;}

    /**
     *      @return none
     *      @param amount Amount to increment object tile by.
     * */
    public void incrementTile( int amount )
    {
        object_num += amount;
    }

}
