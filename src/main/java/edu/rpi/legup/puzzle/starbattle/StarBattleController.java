package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class StarBattleController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        StarBattleCell cell = (StarBattleCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else {
                data.setData(cell.getData() + 1);
                if (cell.getData() >= 0) {
                    data.setData(-3);
                }
            }
        }
        else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() == -3) {
                    data.setData(0);
                }
                else {
                    data.setData(cell.getData() - 1);
                }
            }
        }
    }
}
