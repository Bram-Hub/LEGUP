//StarBattleCellType.java
package edu.rpi.legup.puzzle.starbattle;

public enum StarBattleCellType {
    STAR(-2), BLACK(-1), UNKNOWN(0);

    public int value; 

    StarBattleCellType(int value) {
        this.value = value;
    }
}