package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.model.gameboard.GridBoard;

public class KakurasuBoard extends GridBoard {
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
