package edu.rpi.legup.puzzle.minesweeper;

import java.awt.*;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.*;

public final class MinesweeperUtilities {

    private static final int SURROUNDING_CELL_MIN_INDEX = 0;
    private static final int SURROUNDING_CELL_MAX_INDEX = 9;

    public static Stream<MinesweeperCell> getSurroundingCells(MinesweeperBoard board, MinesweeperCell cell) {
        final Point loc = cell.getLocation();
        final int height = board.getHeight();
        final int width = board.getWidth();
        final int x = (int) loc.getX();
        final int y = (int) loc.getY();
        // IntStream of 0-9 to represent 2D matrix of surrounding elements,
        // this maps from 0,0 to 2,2 so everything needs to be shifted
        // left 1 and up 1 to become
        // -1,1 to 1,1
        // and 5 is skipped because we want to ignore 1,1
        return IntStream.range(SURROUNDING_CELL_MIN_INDEX, SURROUNDING_CELL_MAX_INDEX)
                // skip 0,0 element
                .filter(i -> i != (SURROUNDING_CELL_MAX_INDEX - SURROUNDING_CELL_MIN_INDEX) / 2)
                .mapToObj(index -> {
                    final int newX = index / 3 - 1 + x;
                    final int newY = index % 3 - 1 + y;
                    // only keep valid locations
                    if (newX < 0 || newY < 0 || newX >= width || newY >= height) {
                        return null;
                    }
                    return board.getCell(newX, newY);
                })
                .filter(Objects::nonNull);
    }

    public static int countSurroundingType(MinesweeperBoard board, MinesweeperCell cell, MinesweeperTileType type) {
        final Stream<MinesweeperTileData> stream = getSurroundingCells(board, cell)
                .map(MinesweeperCell::getData);
        return (int) (switch (type) {
            case UNSET -> stream.filter(MinesweeperTileData::isUnset);
            case BOMB -> stream.filter(MinesweeperTileData::isBomb);
            case EMPTY -> stream.filter(MinesweeperTileData::isEmpty);
            case FLAG -> stream.filter(MinesweeperTileData::isFlag);
        }).count();
    }

    public static int countSurroundingBombs(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.BOMB);
    }

    public static int countSurroundingUnset(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.UNSET);
    }

    public static int countSurroundingEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.EMPTY);
    }

    public static int countSurroundingFlags(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.FLAG);
    }

    /**
     *
     * @return how many bombs are left that need to be placed
     * around {@code cell} which must be a flag
     */
    public int countNeededBombsFromFlag(MinesweeperBoard board, MinesweeperCell cell) {
        if (!cell.getData().isFlag()) {
            throw new IllegalArgumentException("Bombs are only needed surrounding flags");
        }
        return cell.getData().data() - countSurroundingBombs(board, cell);
    }

    public static boolean hasEmptyAdjacent(MinesweeperBoard board, MinesweeperCell cell) {
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(board, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.UNSET) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<MinesweeperCell> getAdjacentCells(MinesweeperBoard board, MinesweeperCell cell) {
        ArrayList<MinesweeperCell> adjCells = new ArrayList<MinesweeperCell>();
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                    continue;
                }
                MinesweeperCell adjCell = (MinesweeperCell) board.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell == null) {
                    continue;
                }
                adjCells.add(adjCell);
            }
        }
        return adjCells;
    }

    public static ArrayList<boolean[]> getCombinations(int chosenNumItems, int totalNumItems) {
        ArrayList<boolean[]> combinations = new ArrayList<boolean[]>();

        // calculate all combinations
        boolean[] array = new boolean[totalNumItems];
        recurseCombinations(combinations, 0, chosenNumItems, 0, totalNumItems, array);

        return combinations;
    }

    private static void recurseCombinations(ArrayList<boolean[]> result, int curIndex, int maxBlack, int numBlack, int len, boolean[] workingArray) {
        if (curIndex == len) {
            // complete, but not valid solution
            if (numBlack != maxBlack) {
                return;
            }
            // complete and valid solution
            result.add(workingArray.clone());
            return;
        }
        // there is no chance of completing the required number of solutions, so quit
        if (len - curIndex < maxBlack - numBlack) {
            return;
        }

        if (numBlack < maxBlack) {
            workingArray[curIndex] = true;
            recurseCombinations(result, curIndex+1, maxBlack, numBlack+1, len, workingArray);
        }
        workingArray[curIndex] = false;
        recurseCombinations(result, curIndex+1, maxBlack, numBlack, len, workingArray);
    }


}
