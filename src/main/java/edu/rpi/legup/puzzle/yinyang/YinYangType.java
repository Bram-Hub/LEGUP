package edu.rpi.legup.puzzle.yinyang;

public enum YinYangType {
    UNKNOWN,
    WHITE,
    BLACK;

    public int toValue() {
        return this.ordinal();
    }
}