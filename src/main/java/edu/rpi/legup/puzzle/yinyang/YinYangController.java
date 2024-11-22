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
}