package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.Element;

import java.awt.event.MouseEvent;

public class BattleShipCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, Element data) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                // DO STUFF WHEN ELEMENT IS CLICKED ON
            }
        }
    }
}