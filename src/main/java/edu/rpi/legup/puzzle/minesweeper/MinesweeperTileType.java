package edu.rpi.legup.puzzle.minesweeper;

public enum MinesweeperTileType {

    /** A cell with nothing, value = <= -2 */
    UNSET,

    /** Represents a cell with no bombs in it, value = 0  */
    EMPTY,
    /** A flag has values 1-8 representing how many bombs are touching it, 1 <= value <= 8 */
    FLAG,
    /** A bomb tile that should be marked by nearby flags, value = 1 */
    BOMB
}
