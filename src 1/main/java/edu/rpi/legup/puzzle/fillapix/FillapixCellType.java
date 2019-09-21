package edu.rpi.legup.puzzle.fillapix;

public enum FillapixCellType {
    UNKNOWN(0), BLACK(1), WHITE(2);

    public int value;

    FillapixCellType(int value) {
        this.value = value;
    }

    public String toString() {
        return super.toString().toLowerCase();
    }
}
