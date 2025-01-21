package edu.rpi.legup.puzzle.kakurasu;

public enum KakurasuType {
    UNKNOWN,
    FILLED,
    EMPTY,
    CLUE_NORTH,
    CLUE_EAST,
    CLUE_SOUTH,
    CLUE_WEST;

    public static KakurasuType valueOf(int num) {
        return switch (num) {
            case 1 -> FILLED;
            case 2 -> EMPTY;
            default -> UNKNOWN;
        };
    }
}
