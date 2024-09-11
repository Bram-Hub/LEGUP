package edu.rpi.legup.puzzle.sudoku;

public class ModelSudokuBoard {
    public int getModelRegionNumbers(int index) {
        int columnMod = index % 3 + 1;
        int rowMod = ((index / 9) % 3) * 3;
        return columnMod + rowMod;
    }

    public int getModelRowNumbers(int index) {
        return index % 9 + 1;
    }

    public int getModelColumnNumbers(int index) {
        return index / 9 + 1;
    }
}
