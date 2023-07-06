package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.rules.TooFewBlackCellsContradictionRule;
import edu.rpi.legup.puzzle.fillapix.rules.TooManyBlackCellsContradictionRule;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FillapixUtilities {

    public static boolean isForcedBlack(FillapixBoard board, FillapixCell cell) {
        TooFewBlackCellsContradictionRule tooManyBlackCells = new TooFewBlackCellsContradictionRule();
        FillapixBoard whiteCaseBoard = board.copy();
        FillapixCell whiteCell = (FillapixCell) whiteCaseBoard.getPuzzleElement(cell);
        whiteCell.setType(FillapixCellType.WHITE);
        return tooManyBlackCells.checkContradictionAt(whiteCaseBoard, cell) != null;
    }

    public static boolean isForcedWhite(FillapixBoard board, FillapixCell cell) {
        TooManyBlackCellsContradictionRule tooManyBlackCells = new TooManyBlackCellsContradictionRule();
        FillapixBoard blackCaseBoard = board.copy();
        FillapixCell blackCell = (FillapixCell) blackCaseBoard.getPuzzleElement(cell);
        blackCell.setType(FillapixCellType.BLACK);
        return tooManyBlackCells.checkContradictionAt(blackCaseBoard, cell) != null;
    }

    public static boolean isComplete(Board board, FillapixCell cell) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        int cellNum = cell.getNumber();
        int cellTouchBlack = 0;
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(cellLoc.x + i, cellLoc.y + j);
                // cell is not in board
                if (adjCell == null) {
                    continue;
                }
                if (adjCell.getType() == FillapixCellType.BLACK) {
                    cellTouchBlack++;
                }
            }
        }
        return cellNum == cellTouchBlack;
    }

    public static boolean hasEmptyAdjacent(Board board, FillapixCell cell) {
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(cellLoc.x + i, cellLoc.y + j);
                // cell is not in board
                if (adjCell == null) {
                    continue;
                }
                if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets all cells adjacent to a specific cell. The cell itself will be included.
     */
    public static ArrayList<FillapixCell> getAdjacentCells(Board board, FillapixCell cell) {
        ArrayList<FillapixCell> adjCells = new ArrayList<FillapixCell>();
        FillapixBoard fillapixBoard = (FillapixBoard) board.copy();
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(cellLoc.x + i, cellLoc.y + j);
                if (adjCell == null) {
                    continue;
                }
                adjCells.add(adjCell);
            }
        }
        return adjCells;
    }

    public static boolean compareCases(List<TreeTransition> childTransitions, ArrayList<Board> cases) {
        boolean foundSpot = true;
        for (TreeTransition childTrans : childTransitions) {
            FillapixBoard actCase = (FillapixBoard) childTrans.getBoard();
            boolean foundBoard = false;
            for (Board b : cases) {
                FillapixBoard posCase = (FillapixBoard) b;
                boolean foundAllCells = false;
                if (posCase.getModifiedData().size() == actCase.getModifiedData().size()) {
                    foundAllCells = true;
                    for (PuzzleElement actEle : actCase.getModifiedData()) {
                        FillapixCell actCell = (FillapixCell) actEle;
                        boolean foundCell = false;
                        for (PuzzleElement posEle : posCase.getModifiedData()) {
                            FillapixCell posCell = (FillapixCell) posEle;
                            if (actCell.getType() == posCell.getType() &&
                                    actCell.getLocation().equals(posCell.getLocation())) {
                                foundCell = true;
                                break;
                            }
                        }
                        if (!foundCell) {
                            foundAllCells = false;
                            break;
                        }
                    }
                }
                if (foundAllCells) {
                    foundBoard = true;
                    break;
                }
            }
            if (!foundBoard) {
                foundSpot = false;
                break;
            }
        }
        return foundSpot;
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
     * a distince combination. Each Boolean array will be <code>totalNumObj</code>
     * long and each index will be <code>true<\code> if the corresponding item is
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
}
