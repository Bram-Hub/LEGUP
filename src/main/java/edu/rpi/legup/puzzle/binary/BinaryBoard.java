package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
public class BinaryBoard extends GridBoard {
    private int size;

    public BinaryBoard(int size) {
        super(size, size);
        this.size = size;
    }

    @Override
    public BinaryCell getCell(int x, int y) {
        return (BinaryCell) super.getCell(x, y);
    }

    public Set<BinaryCell> getRow(int rowNum) {
        Set<BinaryCell> row = new HashSet<>();
        for (int i = 0; i < size; i++) {
            row.add(getCell(i, rowNum));
        }
        return row;
    }

    public Set<BinaryCell> getCol(int colNum) {
        Set<BinaryCell> col = new HashSet<>();
        for (int i = 0; i < size; i ++) {
            col.add(getCell(colNum, i));
        }
        return col;
    }
}