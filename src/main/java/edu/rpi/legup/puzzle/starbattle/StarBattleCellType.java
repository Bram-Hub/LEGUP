//StarBattleCellType.java
package edu.rpi.legup.puzzle.starbattle;

public enum StarBattleType {
    UNKNOWN(-3), STAR(-2), BLACK(-1);

    public int value; 

    StarBattleCell(int value) {
        this.value = value;
    }
}