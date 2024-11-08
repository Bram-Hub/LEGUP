package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.GridBoard;

import java.util.ArrayList;

public class KakurasuBoard extends GridBoard {
    // TODO: Add the vertical and horizontal clues to the kakurasu board, and also add the row/column labels

    public KakurasuBoard(int width, int height) {
        super(width, height);
    }
    public KakurasuBoard(int size) {
        super(size);
    }

    @Override
    public KakurasuCell getCell(int x, int y) {
        return (KakurasuCell) super.getCell(x, y);
    }
}
