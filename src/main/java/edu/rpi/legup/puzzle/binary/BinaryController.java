package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.event.MouseEvent;

public class BinaryController extends ElementController {

    /**
     * Handles cell state changes in the binary puzzle when a mouse event occurs
     * If the left mouse button is clicked:
     *   - If the control key is held down, shows a context menu at the mouse position
     *   - Otherwise, toggles the cell data state between 0, 1, and 2 in a cyclic manner
     * If the right mouse button is clicked, the cell state is also toggled between 2, 1, and 0
     *
     * @param e    MouseEvent triggered by the user interaction
     * @param data PuzzleElement representing the cell being modified
     */
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        BinaryCell cell = (BinaryCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView
                        .getSelectionPopupMenu()
                        .show(
                                boardView,
                                this.boardView.getCanvas().getX() + e.getX(),
                                this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getData() == 0) {
                    data.setData(1);
                } else {
                    if (cell.getData() == 1) {
                        data.setData(2);
                    } else {
                        data.setData(0);
                    }
                }
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() == 2) {
                    data.setData(1);
                } else {
                    if (cell.getData() == 1) {
                        data.setData(0);
                    } else {
                        data.setData(2);
                    }
                }
            }
        }
    }
}
