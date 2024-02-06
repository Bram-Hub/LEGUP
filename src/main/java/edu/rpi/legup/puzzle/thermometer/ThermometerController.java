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
                if (cell.getData() == ThermometerFill.EMPTY.ordinal()) {
                    data.setData(ThermometerFill.FILLED.ordinal());
                } else if (cell.getData() == ThermometerFill.FILLED.ordinal()) {
                    data.setData(ThermometerFill.BLOCKED.ordinal());
                } else {
                    data.setData(ThermometerFill.EMPTY.ordinal());
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            if (cell.getData() == ThermometerFill.EMPTY.ordinal()) {
                data.setData(ThermometerFill.BLOCKED.ordinal());
            } else if (cell.getData() == ThermometerFill.BLOCKED.ordinal()) {
                data.setData(ThermometerFill.FILLED.ordinal());
            } else {
                data.setData(ThermometerFill.EMPTY.ordinal());
            }
        }
    }
}