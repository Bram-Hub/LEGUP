package edu.rpi.legup.puzzle.minesweeper;

import java.awt.*;
import java.util.*;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MinesweeperUtilities {

    private static final int SURROUNDING_CELL_MIN_INDEX = 0;
    private static final int SURROUNDING_CELL_MAX_INDEX = 9;

    public static Stream<MinesweeperCell> getSurroundingCells(
            MinesweeperBoard board, MinesweeperCell cell) {
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
                .mapToObj(
                        index -> {
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

    public static int countSurroundingType(
            MinesweeperBoard board, MinesweeperCell cell, MinesweeperTileType type) {
        final Stream<MinesweeperTileData> stream =
                getSurroundingCells(board, cell).map(MinesweeperCell::getData);
        return (int)
                (switch (type) {
                            case UNSET -> stream.filter(MinesweeperTileData::isUnset);
                            case MINE -> stream.filter(MinesweeperTileData::isMine);
                            case EMPTY -> stream.filter(MinesweeperTileData::isEmpty);
                            case NUMBER -> stream.filter(MinesweeperTileData::isNumber);
                        })
                        .count();
    }

    public static int countSurroundingMines(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.MINE);
    }

    public static int countSurroundingUnset(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.UNSET);
    }

    public static int countSurroundingEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.EMPTY);
    }

    public static int countSurroundingNumbers(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.NUMBER);
    }

    /**
     * @return how many mines are left that need to be placed around {@code cell} which must be a
     *     flag
     */
    public int countNeededminesFromNumber(MinesweeperBoard board, MinesweeperCell cell) {
        if (!cell.getData().isNumber()) {
            throw new IllegalArgumentException("mines are only needed surrounding numbers");
        }
        return cell.getData().data() - countSurroundingMines(board, cell);
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

    public static ArrayList<MinesweeperCell> getAdjacentCells(
            MinesweeperBoard board, MinesweeperCell cell) {
        ArrayList<MinesweeperCell> adjCells = new ArrayList<MinesweeperCell>();
        Point cellLoc = cell.getLocation();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (cellLoc.getX() + i < 0
                        || cellLoc.y + j < 0
                        || cellLoc.x + i >= board.getWidth()
                        || cellLoc.y + j >= board.getHeight()) {
                    continue;
                }
                MinesweeperCell adjCell =
                        (MinesweeperCell) board.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell == null || adjCell == cell) {
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

    private static void recurseCombinations(
            ArrayList<boolean[]> result,
            int curIndex,
            int maxBlack,
            int numBlack,
            int len,
            boolean[] workingArray) {
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
            recurseCombinations(result, curIndex + 1, maxBlack, numBlack + 1, len, workingArray);
        }
        workingArray[curIndex] = false;
        recurseCombinations(result, curIndex + 1, maxBlack, numBlack, len, workingArray);
    }

    // checks if the current cell is forced to be a mine by checking if any of its adjacent cells
    // are a number cell that can only be satisfied if the current cell is a mine
    public static boolean isForcedMine(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        emptyCell.setCellType(MinesweeperTileData.mine());
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(emptyCaseBoard, emptyCell);
        int numMines;
        int numUnset;
        int cellNum;
        for (MinesweeperCell adjCell : adjCells) {
            cellNum = adjCell.getTileNumber();
            if (cellNum <= 0) {
                continue;
            }
            numMines = 0;
            numUnset = 0;
            ArrayList<MinesweeperCell> curAdjCells =
                    MinesweeperUtilities.getAdjacentCells(emptyCaseBoard, adjCell);
            for (MinesweeperCell curAdjCell : curAdjCells) {
                if (curAdjCell.getTileType() == MinesweeperTileType.MINE) {
                    numMines++;
                }
                if (curAdjCell.getTileType() == MinesweeperTileType.UNSET) {
                    numUnset++;
                }
            }
            if (cellNum == numUnset + numMines) {
                return true;
            }
        }
        return false;
    }

    // checks if the current cell is forced to be empty by checking if any of its adjacent cells
    // are a number cell that can only be satisfied if the current cell is empty
    public static boolean isForcedEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        emptyCell.setCellType(MinesweeperTileData.empty());
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(emptyCaseBoard, emptyCell);
        int mineCount;
        int adjCellNum;
        int emptyCells = 0;
        for (MinesweeperCell adjCell : adjCells) {
            mineCount = 0;
            adjCellNum = adjCell.getTileNumber();
            if (adjCellNum >= 1) {
                ArrayList<MinesweeperCell> adjAdjCells = getAdjacentCells(emptyCaseBoard, adjCell);
                for (MinesweeperCell adjAdjCell : adjAdjCells) {
                    if (adjAdjCell.getTileType() == MinesweeperTileType.MINE) {
                        mineCount++;
                    }
                }
                if (mineCount == adjCellNum) {
                    return true;
                }
            } else {
                emptyCells++;
            }
        }
        return emptyCells == adjCells.size();
    }

    public static boolean nonTouchingSharedIsMine(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        int x = emptyCell.getLocation().x;
        int y = emptyCell.getLocation().y;
        int height = emptyCaseBoard.getHeight();
        int width = emptyCaseBoard.getWidth();
        int numFar;
        int numClose;



        // Goes through all possible positions that horizontally adjacent number cells
        // could be in that could force the current cell to be a mine. If one possibility
        // actually has the number cells that force the current cell to be a mine, return true
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j++) {
                if (x + (2 * i) >= 0 && x + (2 * i) < width && y + j >= 0 && y + j < height) {
                    numClose = emptyCaseBoard.getCell(x + i, y + j).getTileNumber();
                    numFar = emptyCaseBoard.getCell(x + (2 * i), y + j).getTileNumber();
                    if (j != -1
                            && y + 1 < height
                            && emptyCaseBoard.getCell(x, y + 1).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (j != 1
                            && y - 1 >= 0
                            && emptyCaseBoard.getCell(x, y - 1).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (j != 0
                            && y + (2 * j) < height
                            && y + (2 * j) >= 0
                            && emptyCaseBoard.getCell(x, y + (2 * j)).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (x + (3 * i) >= 0 && x + (3 * i) < width) {
                        if (emptyCaseBoard.getCell(x + (3 * i), y + j).getTileNumber() == -1) {
                            numFar--;
                        }
                        if (y + j + 1 < emptyCaseBoard.getHeight()
                                && emptyCaseBoard.getCell(x + (3 * i), y + j + 1).getTileNumber()
                                        == -1) {
                            numFar--;
                        }
                        if (y + j - 1 >= 0
                                && emptyCaseBoard.getCell(x + (3 * i), y + j - 1).getTileNumber()
                                        == -1) {
                            numFar--;
                        }
                    }
                    if (numClose >= 1 && numFar + 1 == numClose) {
                        return true;
                    }
                }
            }
        }

        // Goes through all possible positions that vertically adjacent number cells
        // could be in that could force the current cell to be a mine. If one possibility
        // actually has the number cells that force the current cell to be a mine, return true
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j++) {
                if (x + j >= 0 && x + j < width && y + (2 * i) >= 0 && y + (2 * i) < height) {
                    numClose = emptyCaseBoard.getCell(x + j, y + i).getTileNumber();
                    numFar = emptyCaseBoard.getCell(x + j, y + (2 * i)).getTileNumber();
                    if (j != -1
                            && x + 1 < width
                            && emptyCaseBoard.getCell(x + 1, y).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (j != 1
                            && x - 1 >= 0
                            && emptyCaseBoard.getCell(x - 1, y).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (j != 0
                            && x + (2 * j) < width
                            && x + (2 * j) >= 0
                            && emptyCaseBoard.getCell(x + (2 * j), y).getTileNumber() <= -1) {
                        numClose--;
                    }
                    if (y + (3 * i) >= 0 && y + (3 * i) < height) {
                        if (emptyCaseBoard.getCell(x + j, y + (3 * i)).getTileNumber() == -1) {
                            numFar--;
                        }
                        if (x + j + 1 < width
                                && emptyCaseBoard.getCell(x + j + 1, y + (3 * i)).getTileNumber()
                                        == -1) {
                            numFar--;
                        }
                        if (x + j - 1 >= 0
                                && emptyCaseBoard.getCell(x + j - 1, y + (3 * i)).getTileNumber()
                                        == -1) {
                            numFar--;
                        }
                    }
                    if (numClose >= 1 && numFar + 1 == numClose) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean nonTouchingSharedIsEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        int x = emptyCell.getLocation().x;
        int y = emptyCell.getLocation().y;
        int height = emptyCaseBoard.getHeight();
        int width = emptyCaseBoard.getWidth();
        int numClose;
        int numFar;

        // Goes through all possible positions that horizontally adjacent number cells
        // could be in that could force the current cell to be empty. If one possibility
        // actually has the number cells that force the current cell to be empty, return true
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j++) {
                if (x + (2 * i) >= 0 && x + (2 * i) < width && y + j >= 0 && y + j < height) {
                    numFar = emptyCaseBoard.getCell(x + i, y + j).getTileNumber();
                    numClose = emptyCaseBoard.getCell(x + (2 * i), y + j).getTileNumber();
                    if (j != -1
                            && y + 1 < height
                            && emptyCaseBoard.getCell(x, y + 1).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 1
                            && y - 1 >= 0
                            && emptyCaseBoard.getCell(x, y - 1).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 0
                            && y + (2 * j) < height
                            && y + (2 * j) >= 0
                            && emptyCaseBoard.getCell(x, y + (2 * j)).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (x + (3 * i) >= 0 && x + (3 * i) < width) {
                        if (emptyCaseBoard.getCell(x + (3 * i), y + j).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if (y + j + 1 < emptyCaseBoard.getHeight()
                                && emptyCaseBoard.getCell(x + (3 * i), y + j + 1).getTileNumber()
                                        <= -1) {
                            numClose--;
                        }
                        if (y + j - 1 >= 0
                                && emptyCaseBoard.getCell(x + (3 * i), y + j - 1).getTileNumber()
                                        <= -1) {
                            numClose--;
                        }
                    }
                    if (numFar >= 1 && numClose == numFar) {
                        return true;
                    }
                }
            }
        }

        // Goes through all possible positions that vertically adjacent number cells
        // could be in that could force the current cell to be empty. If one possibility
        // actually has the number cells that force the current cell to beempty, return true
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j++) {
                if (x + j >= 0 && x + j < width && y + (2 * i) >= 0 && y + (2 * i) < height) {
                    numFar = emptyCaseBoard.getCell(x + j, y + i).getTileNumber();
                    numClose = emptyCaseBoard.getCell(x + j, y + (2 * i)).getTileNumber();
                    if (j != -1
                            && x + 1 < width
                            && emptyCaseBoard.getCell(x + 1, y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 1
                            && x - 1 >= 0
                            && emptyCaseBoard.getCell(x - 1, y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 0
                            && x + (2 * j) < width
                            && x + (2 * j) >= 0
                            && emptyCaseBoard.getCell(x + (2 * j), y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (y + (3 * i) >= 0 && y + (3 * i) < height) {
                        if (emptyCaseBoard.getCell(x + j, y + (3 * i)).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if (x + j + 1 < width
                                && emptyCaseBoard.getCell(x + j + 1, y + (3 * i)).getTileNumber()
                                        <= -1) {
                            numClose--;
                        }
                        if (x + j - 1 >= 0
                                && emptyCaseBoard.getCell(x + j - 1, y + (3 * i)).getTileNumber()
                                        <= -1) {
                            numClose--;
                        }
                    }
                    if (numFar >= 1 && numClose == numFar) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
