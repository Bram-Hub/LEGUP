package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SudokuCell extends GridCell<Integer> {
    private int groupIndex;
    private Set<Integer> annotations;

    /**
     * SudokuCell Constructor - creates a new Sudoku cell to hold the puzzleElement
     *
     * @param value      value of the sudoku cell
     * @param location   location of the cell on the board
     * @param groupIndex index of the group the cell is in on the board
     */
    public SudokuCell(int value, Point location, int groupIndex) {
        super(value, location);
        this.groupIndex = groupIndex;
        this.annotations = new HashSet<>();
    }

    /**
     * Gets the group index of the cell
     *
     * @return group index of the cell
     */
    public int getGroupIndex() {
        return groupIndex;
    }

    public Set<Integer> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<Integer> annotations) {
        this.annotations = annotations;
    }

    /**
     * Performs a deep copy on the SudokuCell
     *
     * @return a new copy of the SudokuCell that is independent of this one
     */
    @Override
    public SudokuCell copy() {
        SudokuCell copy = new SudokuCell(data, (Point) location.clone(), groupIndex);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }
}
