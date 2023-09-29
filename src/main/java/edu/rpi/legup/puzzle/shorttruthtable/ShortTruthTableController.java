package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class ShortTruthTableController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {

        System.out.println("STTController: Cell change");

        // cast the data to a short truth table cell
        ShortTruthTableCell cell = (ShortTruthTableCell) data;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                cell.cycleTypeForward();
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                cell.cycleTypeBackward();
            }
        }
    }
}
