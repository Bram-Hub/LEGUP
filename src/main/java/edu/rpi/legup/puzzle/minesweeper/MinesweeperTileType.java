package edu.rpi.legup.puzzle.minesweeper;

public enum MinesweeperTileType {


    /**
     * A cell with nothing
     */
    UNSET,

    /**
     * Represents a cell with no bombs in  it
     */
    EMPTY,
    /**
     * A flag has values 1-8 representing how many bombs are touching it
     */
    FLAG,
    /**
     * A bomb tile that should be marked by nearby flags
     */
    BOMB

}
