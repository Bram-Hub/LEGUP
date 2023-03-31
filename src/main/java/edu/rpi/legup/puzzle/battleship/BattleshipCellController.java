package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class BattleshipCellController extends ElementController {
    /**
     * Controller class for the Battleship puzzle -
     * receives user mouse input and changes what's shown on the GUI
     *
     * @param data the PuzzleElement to be changed
     * @param e    the user mouse input
     */
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        BattleshipCell cell = (BattleshipCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else {
                if (cell.getData() == BattleshipType.SHIP_MIDDLE) {
                    cell.setData(BattleshipType.UNKNOWN);
                }
                else {
                    cell.setData(BattleshipType.getType(cell.getData().value + 1));
                }
            }
        }
        else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() == BattleshipType.UNKNOWN) {
                    cell.setData(BattleshipType.SHIP_MIDDLE);
                }
                else {
                    cell.setData(BattleshipType.getType(cell.getData().value - 1));
                }
            }
        }
    }
}