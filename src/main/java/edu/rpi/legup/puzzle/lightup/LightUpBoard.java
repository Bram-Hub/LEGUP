package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LightUpBoard extends GridBoard {

    /**
     * LightUpBoard Constructor - creates a LightUpBoard of the specified width and height
     *
     * @param width width of the board
     * @param height height of the board
     */
    public LightUpBoard(int width, int height) {
        super(width, height);
    }

    /**
     * LightUpBoard Constructor - creates a square LightUpBoard of the specified size
     *
     * @param size width and height of the board
     */
    public LightUpBoard(int size) {
        super(size, size);
    }

    /**
     * Sets cells in board to lite depending on whether there is a bulb cell in the current row or
     * column
     */
    public void fillWithLight() {
        for (int y = 0; y < this.dimension.height; y++) {
            for (int x = 0; x < this.dimension.width; x++) {
                getCell(x, y).setLite(false);
            }
        }

        for (int y = 0; y < this.dimension.height; y++) {
            for (int x = 0; x < this.dimension.width; x++) {
                LightUpCell cell = getCell(x, y);
                if (cell.getType() == LightUpCellType.BULB) {
                    cell.setLite(true);
                    for (int i = x + 1; i < this.dimension.width; i++) {
                        LightUpCell c = getCell(i, y);
                        if (c.getType() == LightUpCellType.NUMBER
                                || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = x - 1; i >= 0; i--) {
                        LightUpCell c = getCell(i, y);
                        if (c.getType() == LightUpCellType.NUMBER
                                || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = y + 1; i < this.dimension.height; i++) {
                        LightUpCell c = getCell(x, i);
                        if (c.getType() == LightUpCellType.NUMBER
                                || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = y - 1; i >= 0; i--) {
                        LightUpCell c = getCell(x, i);
                        if (c.getType() == LightUpCellType.NUMBER
                                || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                }
            }
        }
    }

    /**
     * Gets adjancent cells to the specified cell
     *
     * @param cell LightUpCell
     * @return Set of adjacent LightUpCells
     */
    public @NotNull Set<LightUpCell> getAdj(@NotNull LightUpCell cell) {
        Set<LightUpCell> adjCells = new HashSet<>();
        cell = (LightUpCell) getPuzzleElement(cell);

        Point loc = cell.getLocation();
        LightUpCell up = getCell(loc.x, loc.y + 1);
        if (up != null) {
            adjCells.add(up);
        }
        LightUpCell down = getCell(loc.x, loc.y - 1);
        if (down != null) {
            adjCells.add(down);
        }
        LightUpCell right = getCell(loc.x + 1, loc.y);
        if (right != null) {
            adjCells.add(right);
        }
        LightUpCell left = getCell(loc.x - 1, loc.y);
        if (left != null) {
            adjCells.add(left);
        }
        return adjCells;
    }

    /**
     * Gets the number of adjacent cells of the specified type
     *
     * @param cell base cell
     * @param type specified type
     * @return the number of adjacent cells
     */
    public int getNumAdj(@NotNull LightUpCell cell, @NotNull LightUpCellType type) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.getType() == type) {
                num++;
            }
        }
        return num;
    }

    /**
     * Gets the number of adjacent cells
     *
     * @param cell LightUpCell
     * @return number of adjacent cells
     */
    public int getNumAdjLite(@NotNull LightUpCell cell) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.isLite()) {
                num++;
            }
        }
        return num;
    }

    /**
     * Gets the number of adjacent cells that are placeable
     *
     * @param cell specified cell
     * @return number of adjacent cells that are placeable
     */
    public int getNumPlaceable(@NotNull LightUpCell cell) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                num++;
            }
        }
        return num;
    }

    /**
     * Gets the LightUpCell at a specific location
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @return cell at location x, y
     */
    @Override
    public @Nullable LightUpCell getCell(int x, int y) {
        return (LightUpCell) super.getCell(x, y);
    }

    /**
     * Notifies the board of a change to a puzzle element and updates lighting
     *
     * @param puzzleElement the puzzle element that has changed
     */
    @Override
    public void notifyChange(@NotNull PuzzleElement puzzleElement) {
        super.notifyChange(puzzleElement);
        fillWithLight();
    }

    /**
     * Performs a deep copy of the LightUpBoard
     *
     * @return a new copy of the LightUpBoard that is independent of this one
     */
    @Override
    public @NotNull LightUpBoard copy() {
        LightUpBoard copy = new LightUpBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.fillWithLight();
        return copy;
    }
}