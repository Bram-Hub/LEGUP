package edu.rpi.legup.puzzle.skyscrapers;

public enum SkyscrapersCellType {
    EMPTY(-1), NUMBER(0);

    public int value;

    SkyscrapersCellType(int value) {
        this.value = value;
    }
}
