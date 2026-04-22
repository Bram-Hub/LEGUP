package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.sudoku.rules.PossibleCellsForNumberRegionCaseRule;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class PossibleNumberCaseBoard extends CaseBoard {

    private SudokuCell cell;
    private Set<Integer> pickableRegions;
    private Set<Integer> pickableRows;
    private Set<Integer> pickableCols;
    /** is the constructor for finding the possible numbers. */
    public PossibleNumberCaseBoard(
            SudokuBoard baseBoard, PossibleCellsForNumberRegionCaseRule caseRule, SudokuCell cell) {
        super(baseBoard, caseRule);
        this.cell = cell;
        this.pickableRegions = new HashSet<>();
        this.pickableRows = new HashSet<>();
        this.pickableCols = new HashSet<>();
    }

    /**
     * Does the checking to see if a cell is able to be picked.
     *
     * @param puzzleElement the puzzle element to check
     * @param e the mouse event
     * @return true if that cell is pickable and false if not
     */
    @Override
    public boolean isPickable(PuzzleElement puzzleElement, MouseEvent e) {
        if (e == null) {
            return false;
        }

        SudokuCell sudokuCell = (SudokuCell) puzzleElement;
        if (e.isShiftDown()) {
            for (int r : pickableRows) {
                if (r == sudokuCell.getLocation().y) {
                    return true;
                }
            }
        } else {
            if (e.isControlDown()) {
                for (int c : pickableCols) {
                    if (c == sudokuCell.getLocation().x) {
                        return true;
                    }
                }
            } else {
                for (int r : pickableRegions) {
                    if (r == sudokuCell.getGroupIndex()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Adds a region to the pickable regions hash set.
     * @param region region that is being added.
     */

    public void addPickableRegion(int region) {
        this.pickableRegions.add(region);
    }

    /**
     * Adds a row to the pickable rows hash set.
     * @param row row that is being added
     */

    public void addPickableRow(int row) {
        this.pickableRows.add(row);
    }

    /**
     *  Adds a col to the pickable cols hash set.
     * @param col column that is being added.
     */

    public void addPickableCol(int col) {
        this.pickableCols.add(col);
    }

    /**
     *
     * @return the cell given to the class
     */

    public SudokuCell getCell() {
        return cell;
    }

    /**
     *
     * @return the pickable regions hash set.
     */

    public Set<Integer> getPickableRegions() {
        return pickableRegions;
    }

    /**
     *
     * @return the pickable rows hash set.
     */

    public Set<Integer> getPickableRows() {
        return pickableRows;
    }

    /**
     *
     * @return the pickable col hash set.
     */

    public Set<Integer> getPickableCols() {
        return pickableCols;
    }
}
