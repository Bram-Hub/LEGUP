package edu.rpi.legup.puzzle.binary;

public enum BinaryType {
    ZERO,
    ONE,
    UNKNOWN;

    public int toValue() {
        return this.ordinal();
    }
}
