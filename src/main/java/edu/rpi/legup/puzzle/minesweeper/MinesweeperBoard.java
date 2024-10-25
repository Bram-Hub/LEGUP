package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.Set;

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

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public MinesweeperBoard copy() {
        MinesweeperBoard newMinesweeperBoard =
                new MinesweeperBoard(this.dimension.width, this.dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                newMinesweeperBoard.setCell(x, y, getCell(x, y).copy());
            }
        }
        return newMinesweeperBoard;
    }


}
