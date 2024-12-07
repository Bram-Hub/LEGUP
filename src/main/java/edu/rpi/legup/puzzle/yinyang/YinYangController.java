package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class YinYangController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        // Ensure the data is cast to YinYangCell
        if (!(data instanceof YinYangCell)) {
            throw new IllegalArgumentException("Invalid cell type");
        }
        YinYangCell cell = (YinYangCell) data;

        YinYangBoard board = (YinYangBoard) this.boardView.getBoard();

        if (e.getButton() == MouseEvent.BUTTON1) { // Left mouse button
            if (e.isControlDown()) {
                this.boardView
                        .getSelectionPopupMenu()
                        .show(
                                boardView,
                                this.boardView.getCanvas().getX() + e.getX(),
                                this.boardView.getCanvas().getY() + e.getY());
            } else {
                // Cycle cell type: UNKNOWN -> WHITE -> BLACK, respecting rules
                if (cell.getType() == YinYangType.UNKNOWN) {
                    if (canSetType(board, cell, YinYangType.WHITE)) {
                        cell.setType(YinYangType.WHITE);
                    }
                } else if (cell.getType() == YinYangType.WHITE) {
                    if (canSetType(board, cell, YinYangType.BLACK)) {
                        cell.setType(YinYangType.BLACK);
                    }
                } else {
                    cell.setType(YinYangType.UNKNOWN);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) { // Right mouse button
            // Reverse the cycle: UNKNOWN <- WHITE <- BLACK, respecting rules
            if (cell.getType() == YinYangType.UNKNOWN) {
                if (canSetType(board, cell, YinYangType.BLACK)) {
                    cell.setType(YinYangType.BLACK);
                }
            } else if (cell.getType() == YinYangType.BLACK) {
                if (canSetType(board, cell, YinYangType.WHITE)) {
                    cell.setType(YinYangType.WHITE);
                }
            } else {
                cell.setType(YinYangType.UNKNOWN);
            }
        }
        this.boardView.repaint(); // Refresh the view after changes
    }

    /**
     * Validates whether a cell can be set to the specified type without breaking rules.
     *
     * @param board The board on which the change is being made
     * @param cell  The cell to be changed
     * @param type  The type to set
     * @return true if the change is valid, false otherwise
     */
    private boolean canSetType(YinYangBoard board, YinYangCell cell, YinYangType type) {
        // Temporarily set the cell type to validate the board state
        YinYangType originalType = cell.getType();
        cell.setType(type);

        // Validate rules
        boolean isValid = YinYangUtilities.validateNo2x2Blocks(board) &&
                YinYangUtilities.validateConnectivity(board);

        // Revert the cell type to its original state
        cell.setType(originalType);

        return isValid;
    }
}
