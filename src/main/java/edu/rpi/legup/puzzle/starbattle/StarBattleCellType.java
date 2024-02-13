//StarBattleCellType.java
package edu.rpi.legup.puzzle.starbattle;

public enum StarBattleCellType {
    UNKNOWN(-3), STAR(-2), BLACK(-1);

    public int value; 

    StarBattleCellType(int value) {
        this.value = value;
    }
}