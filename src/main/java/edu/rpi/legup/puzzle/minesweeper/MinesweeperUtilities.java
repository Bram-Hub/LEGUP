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
                            case BOMB -> stream.filter(MinesweeperTileData::isBomb);
                            case EMPTY -> stream.filter(MinesweeperTileData::isEmpty);
                            case FLAG -> stream.filter(MinesweeperTileData::isFlag);
                        })
                        .count();
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
     * @return how many bombs are left that need to be placed around {@code cell} which must be a
     *     flag
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

    public static boolean isForcedBomb(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        emptyCell.setCellType(MinesweeperTileData.bomb());
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(emptyCaseBoard, emptyCell);
        for (MinesweeperCell adjCell : adjCells) {
            if(adjCell.getTileNumber() <= 0) {
                continue;
            }
            int cellNum = adjCell.getTileNumber();
            int numBombs = 0;
            int numUnset = 0;
            ArrayList<MinesweeperCell> curAdjCells =
                    MinesweeperUtilities.getAdjacentCells(emptyCaseBoard, adjCell);
            for (MinesweeperCell curAdjCell : curAdjCells) {
                if(curAdjCell.getTileType() == MinesweeperTileType.BOMB) {
                    numBombs++;
                }
                if(curAdjCell.getTileType() == MinesweeperTileType.UNSET) {
                    numUnset++;
                }
            }
            if(cellNum == numUnset + numBombs) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForcedEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        emptyCell.setCellType(MinesweeperTileData.empty());
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(emptyCaseBoard, emptyCell);
        int bombCount;
        int adjCellNum;
        int emptyCells = 0;
        for (MinesweeperCell adjCell : adjCells) {
            bombCount = 0;
            adjCellNum = adjCell.getTileNumber();
            if(adjCellNum >= 1) {
                ArrayList<MinesweeperCell> adjAdjCells = getAdjacentCells(emptyCaseBoard, adjCell);
                for(MinesweeperCell adjAdjCell : adjAdjCells) {
                    if(adjAdjCell.getTileType() == MinesweeperTileType.BOMB) {
                        bombCount++;
                    }
                }
                if(bombCount == adjCellNum) {
                    return true;
                }
            } else {
                emptyCells++;
            }
        }
        return emptyCells == adjCells.size();
    }

    public static boolean oneTwoBomb(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        int x = emptyCell.getLocation().x;
        int y = emptyCell.getLocation().y;
        int height = emptyCaseBoard.getHeight();
        int width = emptyCaseBoard.getWidth();
        int numSmall;
        int numBig;

        // 1 - 2 - X in horizontal direction
        for(int i = -1; i <= 1; i += 2) {
            for(int j = -1; j <= 1; j ++) {
                if(x + (2 * i) >= 0 && x + (2 * i) < width
                    && y + j >= 0 && y + j < height) {
                    numBig = emptyCaseBoard.getCell(x + i, y + j).getTileNumber();
                    numSmall = emptyCaseBoard.getCell(x + (2 * i), y + j).getTileNumber();
                    if(j != -1 && y + 1 < height
                        && emptyCaseBoard.getCell(x, y + 1).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if(j != 1 && y - 1 >= 0 &&
                            emptyCaseBoard.getCell(x, y-1).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if(j != 0 && y + (2 * j) < height
                        && y + (2 * j) >= 0 && emptyCaseBoard.getCell(x, y + (2 * j)).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if(x + (3 * i) >= 0 && x + (3 * i) < width) {
                        if(emptyCaseBoard.getCell(x+(3*i), y+j).getTileNumber() == -1) {
                            numSmall--;
                        }
                        if(y + j + 1 < emptyCaseBoard.getHeight()
                            && emptyCaseBoard.getCell(x+(3*i), y+j+1).getTileNumber() == -1) {
                            numSmall--;
                        }
                        if(y + j - 1 >= 0 &&
                                emptyCaseBoard.getCell(x+(3*i), y+j-1).getTileNumber() == -1) {
                            numSmall--;
                        }
                    }
                    if(numBig >= 1 && numSmall+1 == numBig) {
                        return true;
                    }
                }
            }
        }

        // 1 - 2 - X in vertical direction
        for(int i = -1; i <= 1; i += 2){
            for (int j = -1; j <= 1; j++) {
                if (x + j >= 0 && x + j < width
                        && y + (2 * i) >= 0 && y + (2 * i) < height) {
                    numBig = emptyCaseBoard.getCell(x + j, y + i).getTileNumber();
                    numSmall = emptyCaseBoard.getCell(x + j, y + (2 * i)).getTileNumber();
                    if (j != -1 && x + 1 < width &&
                            emptyCaseBoard.getCell(x + 1, y).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if (j != 1 && x - 1 >= 0 &&
                            emptyCaseBoard.getCell(x - 1, y).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if (j != 0 && x + (2 * j) < width &&
                            x + (2 * j) >= 0 &&
                            emptyCaseBoard.getCell(x + (2 * j), y).getTileNumber() <= -1) {
                        numBig--;
                    }
                    if (y + (3 * i) >= 0 && y + (3 * i) < height) {
                        if (emptyCaseBoard.getCell(x + j, y + (3 * i)).getTileNumber() == -1) {
                            numSmall--;
                        }
                        if (x + j + 1 < width &&
                                emptyCaseBoard.getCell(x + j + 1, y + (3 * i)).getTileNumber() == -1) {
                            numSmall--;
                        }
                        if (x + j - 1 >= 0 && emptyCaseBoard.getCell(x + j - 1, y + (3 * i)).getTileNumber() == -1) {
                            numSmall--;
                        }
                    }
                    if (numBig >= 1 && numSmall + 1 == numBig) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean oneOneEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        MinesweeperBoard emptyCaseBoard = board.copy();
        MinesweeperCell emptyCell = (MinesweeperCell) emptyCaseBoard.getPuzzleElement(cell);
        int x = emptyCell.getLocation().x;
        int y = emptyCell.getLocation().y;
        int height = emptyCaseBoard.getHeight();
        int width = emptyCaseBoard.getWidth();
        int numClose;
        int numFar;

        // 1 - 2 - E in horizontal direction
        for(int i = -1; i <= 1; i += 2) {
            for(int j = -1; j <= 1; j ++) {
                if(x + (2 * i) >= 0 && x + (2 * i) < width
                        && y + j >= 0 && y + j < height) {
                    numFar = emptyCaseBoard.getCell(x + i, y + j).getTileNumber();
                    numClose = emptyCaseBoard.getCell(x + (2 * i), y + j).getTileNumber();
                    if(j != -1 && y + 1 < height
                            && emptyCaseBoard.getCell(x, y + 1).getTileNumber() == -1) {
                        numFar--;
                    }
                    if(j != 1 && y - 1 >= 0 &&
                            emptyCaseBoard.getCell(x, y-1).getTileNumber() == -1) {
                        numFar--;
                    }
                    if(j != 0 && y + (2 * j) < height
                            && y + (2 * j) >= 0 && emptyCaseBoard.getCell(x, y + (2 * j)).getTileNumber() == -1) {
                        numFar--;
                    }
                    if(x + (3 * i) >= 0 && x + (3 * i) < width) {
                        if(emptyCaseBoard.getCell(x+(3*i), y+j).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if(y + j + 1 < emptyCaseBoard.getHeight()
                                && emptyCaseBoard.getCell(x+(3*i), y+j+1).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if(y + j - 1 >= 0 &&
                                emptyCaseBoard.getCell(x+(3*i), y+j-1).getTileNumber() <= -1) {
                            numClose--;
                        }
                    }
                    if(numFar >= 1 && numClose == numFar) {
                        return true;
                    }
                }
            }
        }

        // 1 - 2 - E in vertical direction
        for(int i = -1; i <= 1; i += 2){
            for (int j = -1; j <= 1; j++) {
                if (x + j >= 0 && x + j < width
                        && y + (2 * i) >= 0 && y + (2 * i) < height) {
                    numFar = emptyCaseBoard.getCell(x + j, y + i).getTileNumber();
                    numClose = emptyCaseBoard.getCell(x + j, y + (2 * i)).getTileNumber();
                    if (j != -1 && x + 1 < width &&
                            emptyCaseBoard.getCell(x + 1, y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 1 && x - 1 >= 0 &&
                            emptyCaseBoard.getCell(x - 1, y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (j != 0 && x + (2 * j) < width &&
                            x + (2 * j) >= 0 &&
                            emptyCaseBoard.getCell(x + (2 * j), y).getTileNumber() == -1) {
                        numFar--;
                    }
                    if (y + (3 * i) >= 0 && y + (3 * i) < height) {
                        if (emptyCaseBoard.getCell(x + j, y + (3 * i)).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if (x + j + 1 < width &&
                                emptyCaseBoard.getCell(x + j + 1, y + (3 * i)).getTileNumber() <= -1) {
                            numClose--;
                        }
                        if (x + j - 1 >= 0 && emptyCaseBoard.getCell(x + j - 1, y + (3 * i)).getTileNumber() <= -1) {
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
