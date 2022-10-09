package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class LightUpBoard extends GridBoard {
    public LightUpBoard(int width, int height) {
        super(width, height);
    }

    public LightUpBoard(int size) {
        super(size, size);
    }

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
                        if (c.getType() == LightUpCellType.NUMBER || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = x - 1; i >= 0; i--) {
                        LightUpCell c = getCell(i, y);
                        if (c.getType() == LightUpCellType.NUMBER || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = y + 1; i < this.dimension.height; i++) {
                        LightUpCell c = getCell(x, i);
                        if (c.getType() == LightUpCellType.NUMBER || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                    for (int i = y - 1; i >= 0; i--) {
                        LightUpCell c = getCell(x, i);
                        if (c.getType() == LightUpCellType.NUMBER || c.getType() == LightUpCellType.BLACK) {
                            break;
                        }
                        c.setLite(true);
                    }
                }
            }
        }
    }

    public Set<LightUpCell> getAdj(LightUpCell cell) {
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

    public int getNumAdj(LightUpCell cell, LightUpCellType type) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.getType() == type) {
                num++;
            }
        }
        return num;
    }

    public int getNumAdjLite(LightUpCell cell) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.isLite()) {
                num++;
            }
        }
        return num;
    }

    public int getNumPlacble(LightUpCell cell) {
        int num = 0;
        Set<LightUpCell> adjCells = getAdj(cell);
        for (LightUpCell c : adjCells) {
            if (c.getType() == LightUpCellType.UNKNOWN && !c.isLite()) {
                num++;
            }
        }
        return num;
    }

    @Override
    public LightUpCell getCell(int x, int y) {
        return (LightUpCell) super.getCell(x, y);
    }

    @Override
    public void notifyChange(PuzzleElement puzzleElement) {
        super.notifyChange(puzzleElement);
        fillWithLight();
    }

    @Override
    public LightUpBoard copy() {
        LightUpBoard copy = new LightUpBoard(dimension.width, dimension.height);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
        }
        for(PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        copy.fillWithLight();
        return copy;
    }
}
