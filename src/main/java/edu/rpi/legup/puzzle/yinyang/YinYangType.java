package edu.rpi.legup.puzzle.yinyang;

public enum YinYangType {
    UNKNOWN, // Represents an uninitialized or empty cell
    WHITE,   // Represents a WHITE cell
    BLACK;   // Represents a BLACK cell

    /**
     * Converts the enum value to an integer representation.
     *
     * @return the ordinal value adjusted for Yin Yang data
     */
    public int toValue() {
        return this.ordinal();
    }
}