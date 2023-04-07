package edu.rpi.legup.puzzle.shorttruthtable;

import java.util.Map;
import java.util.HashMap;

public enum ShortTruthTableCellType {

    FALSE(0), TRUE(1), UNKNOWN(-1), NOT_IN_PLAY(-2), PARENTHESIS(-3);

    public int value;
    private static Map map = new HashMap<>();

    ShortTruthTableCellType(int value) {
        this.value = value;
    }

    static {
        for (ShortTruthTableCellType cellType : ShortTruthTableCellType.values()) {
            map.put(cellType.value, cellType);
        }
    }

    public static ShortTruthTableCellType valueOf(int cellType) {
        return (ShortTruthTableCellType) map.get(cellType);
    }

    /**
     * Gets the char value of a cell, Used for debugging
     * @param type cell type input
     */
    public static char toChar(ShortTruthTableCellType type) {
        if (type == TRUE) return 'T';
        if (type == FALSE) return 'F';
        if (type == UNKNOWN) return '?';
        return ' ';
    }


    /**
     * Returns true if this cell holds the value either TRUE or FALSE
     *
     * @return
     */
    public boolean isTrueOrFalse() {
        return value == 0 || value == 1;
    }


    public ShortTruthTableCellType getNegation() {
        switch (value) {
            case 1:
                return ShortTruthTableCellType.FALSE;
            case 0:
                return ShortTruthTableCellType.TRUE;
            default:
                throw new RuntimeException("Trying to negate a cell not assigned to true or false");
        }
    }

    public static ShortTruthTableCellType getDefaultType(char c) {
        if (c == '(' || c == ')') return PARENTHESIS;
        return UNKNOWN;
    }

}