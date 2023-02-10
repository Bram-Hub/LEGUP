package edu.rpi.legup.puzzle.skyscrapers;

public enum SkyscrapersType {
    UNKNOWN(0), Number(1), ANY(2), CLUE_NORTH(-1), CLUE_EAST(-2), CLUE_SOUTH(-3), CLUE_WEST(-4);

    public int value;

    SkyscrapersType(int value) {
        this.value = value;
    }

    public static SkyscrapersType convertToSkyType(int num) {
        if (num == 0) return UNKNOWN;
        else if (num == 1) return Number;
        else if (num == 2) return ANY;
        else if (num == -1) return CLUE_NORTH;
        else if (num == -2) return CLUE_EAST;
        else if (num == -3) return CLUE_SOUTH;
        else if (num == -4) return CLUE_WEST;
        else {
            return UNKNOWN;
        }
    }
}