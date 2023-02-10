package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

public enum MasyuType {
    UNKNOWN, BLACK, WHITE, LINE;

    public static MasyuType convertToMasyuType(int num) {
        if (num == 0) {
            return UNKNOWN;
        } else if (num == 1) {
            return BLACK;
        } else if (num == 2) {
            return WHITE;
        } else if (num == 3) {
            return LINE;
        } else {
            return UNKNOWN;
        }
    }
}

