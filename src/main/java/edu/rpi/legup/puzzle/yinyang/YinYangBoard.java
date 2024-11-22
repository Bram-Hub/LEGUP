package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.List;

public class YinYangBoard extends PuzzleBoard {
    private int width;
    private int height;

    public YinYangBoard(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                YinYangCell cell = new YinYangCell(YinYangType.UNKNOWN.toValue(), x, y);
                addPuzzleElement(cell);
            }
        }
    }

    public YinYangCell getCell(int x, int y) {
        for (PuzzleElement element : getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getX() == x && cell.getY() == y) {
                return cell;
            }
        }
        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}