package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SudokuBoard extends GridBoard {
    private int size;
    private int groupSize;

    /**
     * SudokuBoard Constructor - create a new Sudoku board
     *
     * @param size size of one side of the sudoku board, must be an integer square root
     */
    public SudokuBoard(int size) {
        super(size, size);
        this.size = size;
        this.groupSize = (int) Math.sqrt(dimension.width);
    }

    /**
     * Gets a SudokuCell from the board
     *
     * @param x x location of the cell
     * @param y y location of the cell
     * @return SudokuCell at location (x, y)
     */
    @Override
    public SudokuCell getCell(int x, int y) {
        return (SudokuCell) super.getCell(x, y);
    }

    /**
     * Gets the SudokuCell in the specified group index at the x and y location given
     * The group index must be by less than the width (or height) of the board and the
     * x and y location is relative to the group. This means the x and y values must be
     * less the square root of the width (or height) of the board.
     *
     * @param groupIndex group index of the cell
     * @param x          x location relative to the group
     * @param y          y location relative to the group
     * @return cell in the specified group index at the given x and y location
     */
    public SudokuCell getCell(int groupIndex, int x, int y) {
        return getCell(x + (groupIndex % groupSize) * groupSize, y + (groupIndex / groupSize) * groupSize);
    }

    /**
     * Gets the size of the sudoku board
     * Standard board is 9x9
     *
     * @return size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the minor group size of the sudoku board
     * Standard board is 3x3x3x3
     *
     * @return minor group size
     */
    public int getGroupSize() {
        return groupSize;
    }

    /**
     * Gets all the cells in the specified row
     *
     * @param rowNum row index
     * @return list of all the cells in the row
     */
    public Set<SudokuCell> getRow(int rowNum) {
        Set<SudokuCell> row = new HashSet<>();
        for (int i = 0; i < size; i++) {
            row.add(getCell(i, rowNum));
        }
        return row;
    }

    /**
     * Gets all the cells in the specified column
     *
     * @param colNum column index
     * @return list of all the cells in the column
     */
    public Set<SudokuCell> getCol(int colNum) {
        Set<SudokuCell> col = new HashSet<>();
        for (int i = 0; i < size; i++) {
            col.add(getCell(colNum, i));
        }
        return col;
    }

    /**
     * Gets all the cells in the specified region
     *
     * @param regionNum region index
     * @return list of all the cells in the region
     */
    public Set<SudokuCell> getRegion(int regionNum) {
        Set<SudokuCell> region = new HashSet<>();
        for (int i = 0; i < size; i++) {
            region.add(getCell(regionNum, i % groupSize, i / groupSize));
        }
        return region;
    }

    public Set<SudokuCell> getAffected(SudokuCell cell) {
        Point loc = cell.getLocation();
        cell = getCell(loc.x, loc.y);
        Set<SudokuCell> affected = new HashSet<>();
        affected.addAll(getRegion(cell.getGroupIndex()));
        affected.addAll(getRow(loc.y));
        affected.addAll(getCol(loc.x));

        return affected;
    }

    public Set<Integer> getPossibleValues(SudokuCell cell) {
        Point loc = cell.getLocation();
        cell = getCell(loc.x, loc.y);
        Set<SudokuCell> possible = getAffected(cell);

        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 0; i < size; i++) {
            possibleValues.add(i);
        }
        for (SudokuCell c : possible) {
            possibleValues.remove(c.getData());
        }

        return possibleValues;
    }

    /**
     * Called when a {@link PuzzleElement} data on this has changed and passes in the equivalent puzzle element with
     * the new data.
     *
     * @param puzzleElement equivalent puzzle element with the new data.
     */
    @Override
    public void notifyChange(PuzzleElement puzzleElement) {
        super.notifyChange(puzzleElement);
        Set<SudokuCell> affected = getAffected((SudokuCell) puzzleElement);
        for (SudokuCell c : affected) {
            c.setAnnotations(getPossibleValues(c));
        }
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    @Override
    public SudokuBoard copy() {
        SudokuBoard copy = new SudokuBoard(size);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
