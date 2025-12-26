package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NurikabeBoard extends GridBoard {
    private static final Logger LOGGER = LogManager.getLogger(NurikabeBoard.class.getName());

    public NurikabeBoard(int width, int height) {
        super(width, height);
    }

    public NurikabeBoard(int size) {
        super(size, size);
    }

    @Override
    public NurikabeCell getCell(int x, int y) {
        if (y * dimension.width + x >= puzzleElements.size()
                || x >= dimension.width
                || y >= dimension.height
                || x < 0
                || y < 0) {
            return null;
        }
        return (NurikabeCell) super.getCell(x, y);
    }

    /**
     * Gets the cells as an int array
     *
     * @return int array of values
     */
    public int[][] getIntArray() {
        int[][] arr = new int[dimension.height][dimension.width];
        for (int i = 0; i < dimension.height; i++) {
            for (int k = 0; k < dimension.width; k++) {
                arr[i][k] = getCell(k, i).getData();
            }
        }
        return arr;
    }

    @Override
    public NurikabeBoard copy() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("NurikabeBoard copy()");
        }
        NurikabeBoard copy = new NurikabeBoard(dimension.width, dimension.height);
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
