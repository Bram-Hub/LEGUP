package edu.rpi.legup.puzzle.minesweeper;

import java.awt.*;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

}
