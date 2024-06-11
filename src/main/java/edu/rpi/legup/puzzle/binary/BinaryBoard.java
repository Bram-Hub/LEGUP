package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BinaryBoard extends GridBoard {
    private int size;

    public BinaryBoard(int width, int height) {
        super(width, height);
        this.size = width;
    }

    public BinaryBoard(int size) {
        super(size, size);
        this.size = size;
    }

    @Override
    public BinaryCell getCell(int x, int y) {
        if (y * dimension.width + x >= puzzleElements.size()
                || x >= dimension.width
                || y >= dimension.height
                || x < 0
                || y < 0) {
            return null;
        }
        return (BinaryCell) super.getCell(x, y);
    }

    public Set<BinaryCell> getRowCells(int rowNum) {
        Set<BinaryCell> row = new HashSet<>();
        for (int i = 0; i < size; i++) {
            BinaryCell cell = getCell(i, rowNum);
            row.add(cell);
        }
        return row;
    }

    public ArrayList<BinaryType> getRowTypes(int rowNum) {
        ArrayList<BinaryType> row = new ArrayList<BinaryType>();
        for (int i = 0; i < size; i++) {
            BinaryCell cell = getCell(i, rowNum);
            row.add(cell.getType());
        }
        return row;
    }

    public ArrayList<BinaryType> getColTypes(int colNum) {
        ArrayList<BinaryType> col = new ArrayList<BinaryType>();
        for (int i = 0; i < size; i++) {
            BinaryCell cell = getCell(colNum, i);
            col.add(cell.getType());
        }
        return col;
    }

    public Set<BinaryCell> getColCells(int colNum) {
        Set<BinaryCell> col = new HashSet<>();
        for (int i = 0; i < size; i++) {
            col.add(getCell(colNum, i));
        }
        return col;
    }

    @Override
    public BinaryBoard copy() {
        System.out.println("BinaryBoard copy()");
        BinaryBoard copy = new BinaryBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
