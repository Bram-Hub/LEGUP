package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.rules.TooFewMinesContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.rules.TooManyMinesContradictionRule;
import java.awt.*;
import java.util.*;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MinesweeperUtilities {

    private static final int SURROUNDING_CELL_MIN_INDEX = 0;
    private static final int SURROUNDING_CELL_MAX_INDEX = 9;

    /**
     * Gets all the surrounding cells that touch the current cell.
     * @param board gets the board to read the data from.
     * @param cell that is currently selected for operations
     * @return a stream of surrounding cells
     */
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

    /**
     * Calculates the amount of cells surrounding the given cell of the given type.
     * @param board  gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @param type the type to look for in the surrounding
     * @return the count of the type imputed that surround the current cells
     */

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

    /**
     * Calculates the amount of cells surrounding the given cell that have a mine
     * @param board gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @return the count of the mines that surround the current cells
     */
    public static int countSurroundingMines(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.MINE);
    }

    /**
     *  Calculates the amount of cells surrounding the given cell that are unset
     * @param board gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @return the count of the unset cells that surround the current cells
     */
    public static int countSurroundingUnset(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.UNSET);
    }

    /**
     * Calculates the amount of cells surrounding the given cell that are empty
     * @param board gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @return the count of the empty cells that surround the current cells
     */
    public static int countSurroundingEmpty(MinesweeperBoard board, MinesweeperCell cell) {
        return countSurroundingType(board, cell, MinesweeperTileType.EMPTY);
    }

    /**
     * Calculates the amount of cells surrounding the given cell that are have a number on them
     * @param board gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @return the count of the cells with numbers that surround the current cells
     */

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


    //function might work better just as an if statement connected to countSurroundingEmpty
    /**
     * Calculate if there is an empty adjacent cell and returns true if is
     * @param board board gets the board to read the data from.
     * @param cell the cell that is being calculated about
     * @return if there is an adjacent empty space then return true
     */
    public static boolean hasEmptyAdjacent(MinesweeperBoard board, MinesweeperCell cell) {
        ArrayList<MinesweeperCell> adjCells = getAdjacentCells(board, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.UNSET) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets all the Adjacent Cells of the given cell and puts them into an arraylist.
     * @param board board gets the board to read the data from
     * @param cell the cell that is being calculated about
     * @return An Arraylist that contains all the Adjacent Cells
     */
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

    /**
     * checks if the current cell is forced to be a mine by checking if any of its adjacent cells
     * are a number cell that can only be satisfied if the current cell is a mine
     *
     * @param board board gets the board to read the data from
     * @param cell the cell that is being calculated about
     * @return true id it is forced to be a mine
     */
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

    /**
     * checks if the current cell is forced to be empty by checking if any of its adjacent cells
     *  are a number cell that can only be satisfied if the current cell is empty
     *
     * @param board board gets the board to read the data from
     * @param cell the cell that is being calculated about
     * @return true id it is forced to be a free
     */

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
        return false;
        // return emptyCells == adjCells.size(); return this iff IsolateMine is a rule
    }

    /**
     *  does the check of the board for contradictions
     *
     * @param board to check contradiction
     * @return true if a contradiction occurred
     */

    public static boolean checkBoardForContradiction(MinesweeperBoard board) {
        ContradictionRule tooManyMines = new TooManyMinesContradictionRule();
        ContradictionRule tooFewMines = new TooFewMinesContradictionRule();
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (tooManyMines.checkContradictionAt(board, board.getCell(i, j)) == null
                        || tooFewMines.checkContradictionAt(board, board.getCell(i, j)) == null) {
                    return true;
                }
            }
        }
        return false;
    }
}
