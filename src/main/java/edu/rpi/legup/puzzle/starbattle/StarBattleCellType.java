// StarBattleCellType.java
package edu.rpi.legup.puzzle.starbattle;

public enum StarBattleCellType {
    STAR(-2),
    BLACK(-1),
    UNKNOWN(0),
    HORIZ_BORDER(1),
    VERT_BORDER(2);

    public int value;

    StarBattleCellType(int value) {
        this.value = value;
    }
}
