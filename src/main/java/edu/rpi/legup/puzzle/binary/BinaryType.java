package edu.rpi.legup.puzzle.binary;

/**
 * Enum representing the possible states of a binary puzzle cell
 *
 * <p>The states include: - ZERO: Represents a cell with a value of 0 - ONE: Represents a cell with
 * a value of 1 - UNKNOWN: Represents an empty cell
 */
public enum BinaryType {
    ZERO, // Enum constant 0
    ONE, // Enum constant 1
    UNKNOWN; // Enum constant 2

    /**
     * The `toValue` method returns the ordinal value of the enum constant, which can be used to
     * convert the enum to an integer representation.
     */
    public int toValue() {
        return this.ordinal();
    }
}
