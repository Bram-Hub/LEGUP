package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.util.*;

public class StarBattleBoard extends GridBoard {

    private int size;
    private int puzzleNum;
    protected List<StarBattleRegion> regions;

    // private ArrayList<Integer> groupSizes;

    public StarBattleBoard(int size, int num) {
        super(size, size);
        this.size = size;
        this.puzzleNum = num;
        this.regions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            regions.add(new StarBattleRegion());
        }
    }

    @Override
    public StarBattleCell getCell(int x, int y) {
        return (StarBattleCell) super.getCell(x, y);
    }

    /*
    public StarBattleCell getCell(int groupIndex, int x, int y) {
        return getCell(x + (groupIndex % groupSize) * groupSize, y + (groupIndex / groupSize) * groupSize);
    }*/

    public int getSize() {
        return size;
    }

    public Set<StarBattleCell> getRow(int rowNum) {
        Set<StarBattleCell> row = new HashSet<>();
        for (int i = 0; i < size; i++) {
            row.add(getCell(i, rowNum));
        }
        return row;
    }

    public int getPuzzleNumber() {
        return puzzleNum;
    }

    public Set<StarBattleCell> getCol(int colNum) {
        Set<StarBattleCell> column = new HashSet<>();
        for (int i = 0; i < size; i++) {
            column.add(getCell(colNum, i));
        }
        return column;
    }

    public StarBattleRegion getRegion(int index) {
        if (index >= size) {
            return null;
        }
        return regions.get(index);
    }

    public StarBattleRegion getRegion(StarBattleCell cell) {
        return getRegion(cell.getGroupIndex());
    }

    public void setRegion(int regionNum, StarBattleRegion region) {
        regions.set(regionNum, region);
    }

    public int columnStars(int columnIndex) {
        int stars = 0;
        if (columnIndex < size) {
            for (StarBattleCell c : this.getCol(columnIndex)) {
                if (c.getType() == StarBattleCellType.STAR) {
                    ++stars;
                }
            }
        }
        return stars;
    }

    public int rowStars(int rowIndex) {
        int stars = 0;
        if (rowIndex < size) {
            for (StarBattleCell c : this.getRow(rowIndex)) {
                if (c.getType() == StarBattleCellType.STAR) {
                    ++stars;
                }
            }
        }
        return stars;
    }

    public StarBattleBoard copy() {
        StarBattleBoard copy = new StarBattleBoard(size, puzzleNum);
        for (int x = 0; x < this.dimension.width; x++) {
            for (int y = 0; y < this.dimension.height; y++) {
                copy.setCell(x, y, getCell(x, y).copy());
            }
            if (x < this.regions.size()) {
                copy.regions.add(this.getRegion(x).copy());
            }
        }
        for (PuzzleElement e : modifiedData) {
            copy.getPuzzleElement(e).setModifiable(false);
        }
        return copy;
    }
}
