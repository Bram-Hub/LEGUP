package edu.rpi.legup.puzzle.binary;

public enum BinaryType {
    UNKNOWN, ZERO, ONE, NUMBER;

    public int toValue() {
        return this.ordinal() - 2;
    }
}
