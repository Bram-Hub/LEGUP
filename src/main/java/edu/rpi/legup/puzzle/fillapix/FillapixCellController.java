package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class FillapixCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement puzzleElement) {
        FillapixCell cell = (FillapixCell) puzzleElement;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getType() == FillapixCellType.UNKNOWN) {
                    cell.setType(FillapixCellType.BLACK);
                } else if (cell.getType() == FillapixCellType.BLACK) {
                    cell.setType(FillapixCellType.WHITE);
                } else if (cell.getType() == FillapixCellType.WHITE) {
                    cell.setType(FillapixCellType.UNKNOWN);
                }
            }
        }
    }
}