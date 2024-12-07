package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class YinYangController extends ElementController {
    private YinYang puzzle;

    public YinYangController() {
        this.puzzle = puzzle;
    }

    public YinYang getPuzzle() {
        return this.puzzle;
    }
    
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        // Ensure the data is cast to YinYangCell
        if (!(data instanceof YinYangCell)) {
            throw new IllegalArgumentException("Invalid cell type");
        }
        YinYangCell cell = (YinYangCell) data;

        if (e.getButton() == MouseEvent.BUTTON1) { // Left mouse button
            if (e.isControlDown()) {
                this.boardView
                        .getSelectionPopupMenu()
                        .show(
                                boardView,
                                this.boardView.getCanvas().getX() + e.getX(),
                                this.boardView.getCanvas().getY() + e.getY());
            } else {
                // Cycle cell type: UNKNOWN -> WHITE -> BLACK
                if (cell.getType() == YinYangType.UNKNOWN) {
                    cell.setType(YinYangType.WHITE);
                } else if (cell.getType() == YinYangType.WHITE) {
                    cell.setType(YinYangType.BLACK);
                } else {
                    cell.setType(YinYangType.UNKNOWN);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
            // Reverse the cycle: UNKNOWN <- WHITE <- BLACK
            if (cell.getType() == YinYangType.UNKNOWN) {
                cell.setType(YinYangType.BLACK);
            } else if (cell.getType() == YinYangType.BLACK) {
                cell.setType(YinYangType.WHITE);
            } else {
                cell.setType(YinYangType.UNKNOWN);
            }
        }
        this.boardView.repaint(); // Refresh the view after changes
    }

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