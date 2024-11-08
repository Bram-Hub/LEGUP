package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class KakurasuController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        KakurasuCell cell = (KakurasuCell) data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getType() == KakurasuType.UNKNOWN) {
                    cell.setData(KakurasuType.FILLED.toValue());
                } else if (cell.getType() == KakurasuType.FILLED) {
                    cell.setData(KakurasuType.EMPTY.toValue());
                } else {
                    cell.setData(KakurasuType.UNKNOWN.toValue());
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getType() == KakurasuType.UNKNOWN) {
                cell.setData(KakurasuType.EMPTY.toValue());
            } else if (cell.getType() == KakurasuType.FILLED) {
                cell.setData(KakurasuType.UNKNOWN.toValue());
            } else {
                cell.setData(KakurasuType.FILLED.toValue());
            }
        }
    }
}