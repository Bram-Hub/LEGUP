package edu.rpi.legup.puzzle.nurikabe;

public enum NurikabeType {
    UNKNOWN, BLACK, WHITE, NUMBER;

    public int toValue() {
        return this.ordinal() - 2;
    }
}
