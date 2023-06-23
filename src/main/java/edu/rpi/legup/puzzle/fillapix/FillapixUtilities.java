package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.puzzle.fillapix.rules.TooFewBlackCellsContradictionRule;
import edu.rpi.legup.puzzle.fillapix.rules.TooManyBlackCellsContradictionRule;

import java.awt.*;

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

    public static boolean isComplete(FillapixBoard board, FillapixCell cell) {
        int cellNum = cell.getNumber();
        int cellTouchBlack = 0;
        Point cellLoc = cell.getLocation();
        for (int i=-1; i <= 1; i++) {
            for (int j=-1; j <= 1; j++) {
                if (board.getCell(cellLoc.x + i, cellLoc.y + j).getType() == FillapixCellType.BLACK) {
                    cellTouchBlack++;
                }
            }
        }
        return cellNum == cellTouchBlack;
    }
}
