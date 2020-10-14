package edu.rpi.legup.puzzle.skyscrapers;

public enum SkyscrapersCellType {
    EMPTY(0), NUMBER(1), CLUE_NORTH(-1), CLUE_SOUTH(-2), CLUE_EAST(-3), CLUE_WEST(-4);

    public int value;

    SkyscrapersCellType(int value) {
        this.value = value;
    }
}
