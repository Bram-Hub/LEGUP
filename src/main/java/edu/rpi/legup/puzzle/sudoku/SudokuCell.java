package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class SudokuCell extends GridCell<Integer> {
    private int groupIndex;
    private Set<Integer> annotations;
    private int max;

    /**
     * SudokuCell Constructor - creates a new Sudoku cell to hold the puzzleElement
     *
     * @param value value of the sudoku cell
     * @param location location of the cell on the board
     * @param groupIndex index of the group the cell is in on the board
     * @param size size of the sudoku cell
     */
    public SudokuCell(int value, Point location, int groupIndex, int size) {
        super(value, location);
        this.groupIndex = groupIndex;
        this.annotations = new HashSet<>();
        this.max = size;
    }

    /**
     * Gets the group index of the cell
     *
     * @return group index of the cell
     */
    public int getGroupIndex() {
        return groupIndex;
    }

    public int getMax() {
        return max;
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
        SudokuCell copy = new SudokuCell(data, (Point) location.clone(), groupIndex, max);
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    /**
     * Sets the type of this NurikabeCell
     *
     * @param e element to set the type of this nurikabe cell to
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        if (e.getElementName().equals("Number Tile")) {
            if (m.getButton() == MouseEvent.BUTTON1) {
                if (this.data <= 0 || this.data > 8) {
                    this.data = 1;
                } else {
                    this.data = this.data + 1;
                }
            } else {
                if (m.getButton() == MouseEvent.BUTTON3) {
                    if (this.data > 1) {
                        this.data = this.data - 1;
                    } else {
                        this.data = 9;
                    }
                }
            }
        } else if (e.getElementName().equals("Unknown Tile")) {
            this.data = 0;
        }
    }

    @Override
    public boolean isKnown() {return !(data == 0);}

    @Override
    public String describeState(boolean isPlural) {
        return switch(data) {
            case 1 -> "one";
            case 2 -> "two";
            case 3 -> "three";
            case 4 -> "four";
            case 5 -> "five";
            case 6 -> "six";
            case 7 -> "seven";
            case 8 -> "eight";
            case 9 -> "nine";
            default -> data.toString();
        };
    }
}
