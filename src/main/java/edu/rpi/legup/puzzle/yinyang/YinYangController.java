package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.BoardController;

public class YinYangController extends BoardController {

    @Override
    public boolean validateBoard() {
        YinYangBoard board = (YinYangBoard) getPuzzle().getCurrentBoard();

        // Validate the puzzle rules
        if (!YinYangUtilities.validateNo2x2Blocks(board)) {
            System.out.println("Invalid board: 2x2 blocks detected!");
            return false;
        }

        if (!YinYangUtilities.validateConnectivity(board)) {
            System.out.println("Invalid board: disconnected groups detected!");
            return false;
        }

        return true;
    }

    @Override
    public void toggleCellType(int x, int y) {
        YinYangBoard board = (YinYangBoard) getPuzzle().getCurrentBoard();
        YinYangCell cell = board.getCell(x, y);

        if (cell != null) {
            // Cycle between UNKNOWN -> WHITE -> BLACK
            if (cell.getType() == YinYangType.UNKNOWN) {
                cell.setType(YinYangType.WHITE);
            } else if (cell.getType() == YinYangType.WHITE) {
                cell.setType(YinYangType.BLACK);
            } else {
                cell.setType(YinYangType.UNKNOWN);
            }
            updateView();
        }
    }

    @Override
    public void resetBoard() {
        YinYangBoard board = (YinYangBoard) getPuzzle().getCurrentBoard();
        for (PuzzleElement element : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            cell.setType(YinYangType.UNKNOWN);
        }
        updateView();
    }

    @Override
    public boolean isPuzzleSolved() {
        YinYangBoard board = (YinYangBoard) getPuzzle().getCurrentBoard();
        return YinYangUtilities.validateNo2x2Blocks(board) &&
                YinYangUtilities.validateConnectivity(board);
    }
}