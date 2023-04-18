package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.puzzle.masyu.MasyuType;

public enum TreeTentType {
    UNKNOWN, TREE, GRASS, TENT,
    CLUE_NORTH, CLUE_EAST, CLUE_SOUTH, CLUE_WEST;

    public static TreeTentType valueOf(int num) {
        switch (num) {
            case 1:
                return TREE;
            case 2:
                return GRASS;
            case 3:
                return TENT;
            default:
                return UNKNOWN;
        }
    }

    public static int valueToInt(String value) {
        switch(value) {
            case "TREE":
                return 1;
            case "GRASS":
                return 2;
            case "TENT":
                return 3;
            default:
                return 0;
        }
    }
}