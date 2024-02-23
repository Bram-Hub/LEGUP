package edu.rpi.legup.puzzle.minesweeper;

import java.awt.*;
import java.util.*;

public class MinesweeperUtilities {
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
