package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class ThermometerController extends ElementController {

    //method for updating thermometer cells since number cells have unknown for
    //their fill type we don't need to worry about end user modifying them with this
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        ThermometerCell cell = (ThermometerCell) data;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else {
                if (cell.getFill() == ThermometerFill.EMPTY) {
                    cell.setFill(ThermometerFill.FILLED);
                }
                else if (cell.getFill() == ThermometerFill.FILLED) {
                    cell.setFill(ThermometerFill.BLOCKED);
                }
                else {
                    cell.setFill(ThermometerFill.EMPTY);
                }
            }
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getFill() == ThermometerFill.EMPTY) {
                cell.setFill(ThermometerFill.BLOCKED);
            }
            else if (cell.getFill() == ThermometerFill.BLOCKED) {
                cell.setFill(ThermometerFill.FILLED);
            }
            else {
                cell.setFill(ThermometerFill.EMPTY);
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON2) {
            System.out.println("[DEBUG] " + cell);
        }
    }
}