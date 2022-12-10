package edu.rpi.legup.puzzle.skyscrapers;

public enum SkyscrapersType {
    UNKNOWN(0), Number(1), ANY(2), CLUE_NORTH(-1), CLUE_EAST(-2), CLUE_SOUTH(-3), CLUE_WEST(-4);

    public int value;

    SkyscrapersType(int value) {
        this.value = value;
    }
}