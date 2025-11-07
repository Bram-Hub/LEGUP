package edu.rpi.legup.puzzle.minesweeper;

public enum MinesweeperTileType {

    /** A cell that is unknown by the user, value = -2 */
    UNSET,
    /** Represents a cell with no mine in it, value = 0 */
    EMPTY,
    /** A number cell has values 1-8 representing how many mines are touching it, 1 to 8 inclusive */
    NUMBER,
    /** A mine cell that should be marked by nearby numbers, value = -1 */
    MINE
}
