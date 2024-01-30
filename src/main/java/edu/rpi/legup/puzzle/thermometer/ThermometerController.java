package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class ThermometerController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        ThermometerCell cell = (ThermometerCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getData() == 0) {
                    data.setData(1);
                } else if (cell.getData() == 1) {
                    data.setData(2);
                } else if (cell.getData() == 2){
                  data.setData(0);
                } else {
                    data.setData(1);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getData() == 1) {
                data.setData(2);
            } else if (cell.getData() == 2) {
                data.setData(3);
            } else if(cell.getData() == 3){
                data.setData(1);
            } else {
                data.setData(1);
            }
        }
    }
}