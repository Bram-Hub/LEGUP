package edu.rpi.legup.puzzle.fillapix;


public enum FillapixTileType {

    /**
     * A cell that has not yet been determined, value = -2
     */
    UNSET,

    /**
     * A white (empty) cell, value = 0
     */
    WHITE,

    /**
     * A black (filled) cell, value = -1
     */
    BLACK,

    /**
     * A clue cell with a value 0–9 indicating number of black cells in the surrounding 3×3 area
     */
    NUMBER
}
