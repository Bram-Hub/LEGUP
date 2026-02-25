package edu.rpi.legup.puzzle.sudoku;

public class ModelSudokuBoard {
    /**
     * gets the 3x3 segment the location in
     * @param index the index of a cell.
     * @return Regional numbers for the model size
     */
    public int getModelRegionNumbers(int index) {
        int columnMod = index % 3 + 1;
        int rowMod = ((index / 9) % 3) * 3;
        return columnMod + rowMod;
    }

    /**
     * get which row by the taking the modulus down from column
     * @param index of the cell
     * @return the row number
     */

    public int getModelRowNumbers(int index) {
        return index % 9 + 1;
    }

    /**
     * Gets the col by breaking up the groups into which group of 9 the cell is in.
     * @param index of the cell
     * @return the column number
     */
    public int getModelColumnNumbers(int index) {
        return index / 9 + 1;
    }
}
