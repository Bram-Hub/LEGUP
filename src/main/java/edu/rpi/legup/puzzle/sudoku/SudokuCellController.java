package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.awt.event.MouseEvent;

public class SudokuCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        SudokuCell cell = (SudokuCell) data;
        //System.out.print(111);
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {
                this.boardView
                        .getSelectionPopupMenu()
                        .show(
                                boardView,
                                this.boardView.getCanvas().getX() + e.getX(),
                                this.boardView.getCanvas().getY() + e.getY());
            } else {
                if (cell.getData() < cell.getMax()) {
                    data.setData(cell.getData() + 1);
                } else {
                    data.setData(0);
                }
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() > 0) {
                    data.setData(cell.getData() - 1);
                } else {
                    data.setData(cell.getMax());
                }
            }
        }
    }
}
