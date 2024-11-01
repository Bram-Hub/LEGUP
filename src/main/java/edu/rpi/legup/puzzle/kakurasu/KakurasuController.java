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
                if (cell.getData() == KakurasuType.UNKNOWN) {
                    data.setData(KakurasuType.FILLED);
                } else if (cell.getData() == KakurasuType.FILLED) {
                    data.setData(KakurasuType.EMPTY);
                } else {
                    data.setData(KakurasuType.UNKNOWN);
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            if (cell.getData() == KakurasuType.UNKNOWN) {
                data.setData(KakurasuType.EMPTY);
            } else if (cell.getData() == KakurasuType.FILLED) {
                data.setData(KakurasuType.UNKNOWN);
            } else {
                data.setData(KakurasuType.FILLED);
            }
        }
    }
}