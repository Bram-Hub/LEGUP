package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.elements.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * GridBoard represents a grid-based board where each cell can be manipulated based on its
 * coordinates. The board supports operations such as getting and setting cells, and provides
 * dimensions of the grid. It also supports deep copying of the board.
 */
public class GridBoard extends Board {

    protected Dimension dimension;

    /**
     * {@code GridBoard} constructor creates a board for grid using puzzles from a width and height.
     *
     * @param width width of the board
     * @param height height of the board
     */
    public GridBoard(int width, int height) {
        this.dimension = new Dimension(width, height);

        for (int i = 0; i < width * height; i++) {
            puzzleElements.add(null);
        }
    }

    /**
     * {@code GridBoard} constructor creates a board for grid using puzzles from a size.
     *
     * @param size width and height of the {@code GridBoard}
     */
    public GridBoard(int size) { this(size, size); }

    /**
     * Gets a {@code GridCell} from the board.
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @return grid cell at location {@code (x, y)}
     */
    public GridCell getCell(int x, int y) {
        if (y * dimension.width + x >= puzzleElements.size()
                || x >= dimension.width
                || y >= dimension.height
                || x < 0
                || y < 0) {
            return null;
        }
        return (GridCell) puzzleElements.get(y * dimension.width + x);
    }

    /**
     * Gets a {@code GridCell} from the board.
     *
     * @param point x and y location of the cell
     * @return grid cell at {@code point}
     */
    public GridCell getCell(@NotNull Point point) { return getCell(point.x, point.y); }

    /**
     * Sets the {@code GridCell} at the location {@code (x,y)}. This method does not set the cell if the
     * location specified is out of bounds.
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @param cell grid cell to set at location {@code (x,y)}
     */
    public void setCell(int x, int y, @NotNull GridCell cell) {
        if (y * dimension.width + x >= puzzleElements.size()
                || x >= dimension.width
                || y >= dimension.height
                || x < 0
                || y < 0) {
            return;
        }
        puzzleElements.set(y * dimension.width + x, cell);
    }

    /**
     * Sets the {@code GridCell} at the location {@code (x,y)}. This method does not set the cell if the
     * location specified is out of bounds.
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @param e puzzle element to set cell to
     * @param m mouse click event, used to differentiate left/right click
     */
    public void setCell(int x, int y, @Nullable Element e, @NotNull MouseEvent m) {

        if (e != null && x < dimension.width && y < dimension.height && x >= 0 && y >= 0) {
            puzzleElements.get(y * dimension.width + x).setType(e, m);
        }
    }

    /**
     * Gets the width of the board.
     *
     * @return width of the board
     */
    public int getWidth() { return dimension.width; }

    /**
     * Gets the height of the board.
     *
     * @return height of the board
     */
    public int getHeight() { return dimension.height; }

    /**
     * Gets the dimension of the grid board.
     *
     * @return the dimension of the grid board
     */
    @NotNull public Dimension getDimension() { return dimension; }

    @Override
    @NotNull public GridBoard copy() {
        GridBoard newGridBoard = new GridBoard(this.dimension.width, this.dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                newGridBoard.setCell(x, y, getCell(x, y).copy());
            }
        }
        return newGridBoard;
    }
}
