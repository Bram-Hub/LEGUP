package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.gameboard.GridBoard;

public class MinesweeperBoard extends GridBoard {

    public MinesweeperBoard(int width, int height) {
        super(width, height);
    }

    public MinesweeperBoard(int size) {
        super(size);
    }

    @Override
    public MinesweeperCell getCell(int x, int y) {
        return (MinesweeperCell) super.getCell(x, y);
    }
}
