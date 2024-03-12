package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class BinaryController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        BinaryCell cell = (BinaryCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else {
                if (cell.getData() == 0) {
                    data.setData(1);
                }
                else {
                    if (cell.getData() == 1) {
                        data.setData(2);
                    }
                    else {
                        data.setData(0);
                    }
                }
            }
        }
        else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() == 0) {
                    data.setData(1);
                }
                else {
                    if (cell.getData() == 1) {
                        data.setData(2);
                    }
                    else {
                        data.setData(0);
                    }
                }
            }
        }
    }
}
