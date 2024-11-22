package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.gameboard.PuzzleElement;

public class YinYangCell extends PuzzleElement {
    private YinYangType type;
    private int x, y;

    public YinYangCell(int data, int x, int y) {
        super(data, null);
        this.x = x;
        this.y = y;
        this.type = YinYangType.UNKNOWN;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public YinYangType getType() {
        return type;
    }

    public void setType(YinYangType type) {
        this.type = type;
    }
}
