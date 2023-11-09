package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.fillapix.rules.TooFewBlackCellsContradictionRule;
import edu.rpi.legup.puzzle.fillapix.rules.TooManyBlackCellsContradictionRule;

import java.awt.*;
import java.util.ArrayList;

public class FillapixUtilities {

    public static boolean isForcedBlack(FillapixBoard board, FillapixCell cell) {
        TooFewBlackCellsContradictionRule tooManyBlackCells = new TooFewBlackCellsContradictionRule();
        FillapixBoard whiteCaseBoard = board.copy();
        FillapixCell whiteCell = (FillapixCell) whiteCaseBoard.getPuzzleElement(cell);
        whiteCell.setCellType(FillapixCellType.WHITE);
        ArrayList<FillapixCell> adjCells = getAdjacentCells(whiteCaseBoard, whiteCell);
        for (FillapixCell adjCell : adjCells) {
            if (tooManyBlackCells.checkContradictionAt(whiteCaseBoard, adjCell) == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForcedWhite(FillapixBoard board, FillapixCell cell) {
        TooManyBlackCellsContradictionRule tooManyBlackCells = new TooManyBlackCellsContradictionRule();
        FillapixBoard blackCaseBoard = board.copy();
        FillapixCell blackCell = (FillapixCell) blackCaseBoard.getPuzzleElement(cell);
        blackCell.setCellType(FillapixCellType.BLACK);
        ArrayList<FillapixCell> adjCells = getAdjacentCells(blackCaseBoard, blackCell);
        for (FillapixCell adjCell : adjCells) {
            if (tooManyBlackCells.checkContradictionAt(blackCaseBoard, adjCell) == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isComplete(FillapixBoard board, FillapixCell cell) {
        int cellNum = cell.getNumber();
        int cellTouchBlack = 0;
        ArrayList<FillapixCell> adjCells = getAdjacentCells(board, cell);
        for (FillapixCell adjCell : adjCells) {
            if (adjCell.getType() == FillapixCellType.BLACK) {
                cellTouchBlack++;
            }
        }
        return cellNum == cellTouchBlack;
    }

    public static boolean hasEmptyAdjacent(FillapixBoard board, FillapixCell cell) {
        ArrayList<FillapixCell> adjCells = getAdjacentCells(board, cell);
        for (FillapixCell adjCell : adjCells) {
            if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all cells adjacent to a specific cell. The cell itself will be included.
     */
    public static ArrayList<FillapixCell> getAdjacentCells(FillapixBoard board, FillapixCell cell) {
        ArrayList<FillapixCell> adjCells = new ArrayList<FillapixCell>();
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                    continue;
                }
                FillapixCell adjCell = board.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell == null) {
                    continue;
                }
                adjCells.add(adjCell);
            }
        }
        return adjCells;
    }

    /**
     * Gets all cells that are contained in the square defined as having 'distance'
     * cells between the center and the outer wall. For example, distance = 1:<p>
     * |X|X|X|X|X|          <p>
     * |X| | | |X|          <p>
     * |X| |O| |X|          <p>
     * |X| | | |X|          <p>
     * |X|X|X|X|X|          <p>
     * O is 'cell', and all 'X' will be returned in the ArrayList
     */
    public static ArrayList<FillapixCell> getCellsAtDistance(FillapixBoard board, FillapixCell cell, int distance) {
        ArrayList<FillapixCell> adjCells = new ArrayList<FillapixCell>();
        Point cellLoc = cell.getLocation();
        int i = 0, j = 0;
        // top line
        for (i = cellLoc.x - (distance), j = cellLoc.y - (distance+1); i <= cellLoc.x + (distance+1); i++) {
            if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                continue;
            }
            FillapixCell adjCell = board.getCell(cellLoc.x + i, cellLoc.y + j);
            if (adjCell == null) {
                continue;
            }
            adjCells.add(adjCell);
        }
        // right line
        for (i = cellLoc.x + (distance+1), j = cellLoc.y - (distance); j <= cellLoc.y + (distance+1); j++) {
            if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                continue;
            }
            FillapixCell adjCell = board.getCell(cellLoc.x + i, cellLoc.y + j);
            if (adjCell == null) {
                continue;
            }
            adjCells.add(adjCell);
        } 
        // bottom line
        for (i = cellLoc.x + (distance), j = cellLoc.y + (distance+1); i <= cellLoc.x - (distance+1); i--) {
            if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                continue;
            }
            FillapixCell adjCell = board.getCell(cellLoc.x + i, cellLoc.y + j);
            if (adjCell == null) {
                continue;
            }
            adjCells.add(adjCell);
        } 
        // left line
        for (i = cellLoc.x - (distance+1), j = cellLoc.y + (distance); j <= cellLoc.y - (distance+1); j--) {
            if (cellLoc.getX() + i < 0 || cellLoc.y + j < 0 || cellLoc.x + i >= board.getWidth() || cellLoc.y + j >= board.getHeight()) {
                continue;
            }
            FillapixCell adjCell = board.getCell(cellLoc.x + i, cellLoc.y + j);
            if (adjCell == null) {
                continue;
            }
            adjCells.add(adjCell);
        } 

        return adjCells;
    } 

    /**
     * Finds all possible combinations of <code>chosenNumObj</code> items can be
     * chosen from <code>totalNumObj</code> total items.
     * For example, if 1 item is chosen from 2 possible items, the combinations 
     * are:
     * <pre>[ [true,false], [false,true] ]</pre>
     * 
     * @param totalNumItems the total number of items that can possibly be chosen
     * @param chosenNumItems the number of items to be chosen
     * 
     * @return an ArrayList of Boolean arrays. Each index in the ArrayList represents
     * a distinct combination. Each Boolean array will be <code>totalNumItems</code>
     * long and each index will be <code>true</code> if the corresponding item is
     * included in that combination, and <code>false</code> otherwise.
     */
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
    
    public static boolean checkBoardForContradiction(FillapixBoard board) {
        ContradictionRule tooManyBlack = new TooManyBlackCellsContradictionRule();
        ContradictionRule tooManyWhite = new TooFewBlackCellsContradictionRule();
        for (int i= 0; i < board.getWidth(); i++) {
            for (int j=0; j < board.getHeight(); j++) {
                if (tooManyBlack.checkContradictionAt(board, board.getCell(i, j)) == null ||
                        tooManyWhite.checkContradictionAt(board, board.getCell(i, j)) == null) {
                    return true;
                }
            }
        }
        return false;
    }
}
