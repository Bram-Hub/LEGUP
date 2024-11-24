package edu.rpi.legup.puzzle.fillapix;

public enum FillapixCellType {
    UNKNOWN(-3),
    BLACK(-2),
    WHITE(-1);

    public int value;

    FillapixCellType(int value) {
        this.value = value;
    }
}
