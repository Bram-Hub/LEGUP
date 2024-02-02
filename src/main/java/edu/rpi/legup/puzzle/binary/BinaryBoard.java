package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public class BinaryBoard extends GridBoard {
    public BinaryBoard(int width, int height) {
        super(width, height);
    }

    public BinaryBoard(int size) {
        super(size, size);
    }

    @Override
    public BinaryCell getCell(int x, int y) {
        return (BinaryCell) super.getCell(x, y);
    }
}