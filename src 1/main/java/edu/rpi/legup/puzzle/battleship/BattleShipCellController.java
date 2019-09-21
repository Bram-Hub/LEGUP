package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class BattleShipCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        BattleShipCell cell = (BattleShipCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getData() == BattleShipCellType.SHIP_SEGMENT_MIDDLE.value) {
                    cell.setData(BattleShipCellType.UNKNOWN.value);
                } else {
                    cell.setData(cell.getData() + 1);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getData() == BattleShipCellType.UNKNOWN.value) {
                cell.setData(BattleShipCellType.SHIP_SEGMENT_MIDDLE.value);
            } else {
                cell.setData(cell.getData() - 1);
            }
        }
    }
}