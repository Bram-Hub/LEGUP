package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.sudoku.rules.PossibleNumberCaseRule;

import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

public class PossibleNumberCaseBoard extends CaseBoard {

    private SudokuCell cell;
    private Set<Integer> pickableRegions;
    private Set<Integer> pickableRows;
    private Set<Integer> pickableCols;


    public PossibleNumberCaseBoard(SudokuBoard baseBoard, PossibleNumberCaseRule caseRule, SudokuCell cell) {
        super(baseBoard, caseRule);
        this.cell = cell;
        this.pickableRegions = new HashSet<>();
        this.pickableRows = new HashSet<>();
        this.pickableCols = new HashSet<>();
    }

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
        } else if (e.isControlDown()) {
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
        return false;
    }

    public void addPickableRegion(int region) {
        this.pickableRegions.add(region);
    }

    public void addPickableRow(int row) {
        this.pickableRows.add(row);
    }

    public void addPickableCol(int col) {
        this.pickableCols.add(col);
    }

    public SudokuCell getCell() {
        return cell;
    }

    public Set<Integer> getPickableRegions() {
        return pickableRegions;
    }

    public Set<Integer> getPickableRows() {
        return pickableRows;
    }

    public Set<Integer> getPickableCols() {
        return pickableCols;
    }
}
