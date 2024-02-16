package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;

public class MinesweeperBoard extends GridBoard {

    public MinesweeperBoard(int width, int height) {
        super(width, height);
    }

    public MinesweeperBoard(int size) {
        super(size);
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public MinesweeperBoard copy() {
        MinesweeperBoard copy = new MinesweeperBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
