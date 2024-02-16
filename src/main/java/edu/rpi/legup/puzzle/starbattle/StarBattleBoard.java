package edu.rpi.legup.puzzle.starbattle;

import java.util.*;

import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;

public class StarBattleBoard extends GridBoard {

    private int size;
    //private ArrayList<Integer> groupSizes;

    public StarBattleBoard(int size) {
        super(size, size);
        this.size = size;
    }

    @Override
    public StarBattleCell getCell(int x, int y) {
        return (StarBattleCell) super.getCell(x,y);
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

    public Set<StarBattleCell> getCol(int colNum) {
        Set<StarBattleCell> column = new HashSet<>();
        for (int i = 0; i < size; i++) {
            column.add(getCell(colNum, i));
        }
        return column;
    }
}


