package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class LightUpCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        LightUpCell cell = (LightUpCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getData() == -2) {
                    data.setData(-4);
                } else if (cell.getData() == -4) {
                    data.setData(-3);
                } else {
                    data.setData(-2);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getData() == -4) {
                data.setData(-2);
            } else if (cell.getData() == -2) {
                data.setData(-3);
            } else {
                data.setData(-4);
            }
        }
    }
}
