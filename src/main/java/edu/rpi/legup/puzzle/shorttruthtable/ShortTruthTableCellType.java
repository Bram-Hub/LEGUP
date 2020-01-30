package edu.rpi.legup.puzzle.shorttruthtable;

import java.util.Map;
import java.util.HashMap;

public enum ShortTruthTableCellType{
	FALSE(0), TRUE(1), UNKNOWN(-1), NOT_IN_PLAY(-2);


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

    public static char toChar(ShortTruthTableCellType type){
        switch(type){
            case TRUE: return 'T';
            case FALSE: return 'F';
            case UNKNOWN: return '?';
            default: return ' ';
        }
    }


    /**
     * Returns true if this cell holds the value either TRUE or FALSE
     *
     * @return
     */
    public boolean isTrueOrFalse(){
        return value==0 || value==1;
    }


    public ShortTruthTableCellType getNegation(){
        switch(value){
            case 1: return ShortTruthTableCellType.FALSE;
            case 0: return ShortTruthTableCellType.TRUE;
            default: throw new RuntimeException("Trying to negate a cell not assigned to true or false");
        }
    }

}