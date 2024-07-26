package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.jetbrains.annotations.Contract;

import java.awt.event.MouseEvent;

public class SkyscrapersController extends ElementController {

    public SkyscrapersController() {
        super();
    }

    @Override
    @Contract(pure = false)
    public void changeCell(MouseEvent e, PuzzleElement element) {
        SkyscrapersCell cell = (SkyscrapersCell) element;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (cell.getData() < cell.getMax()) {
                int num = cell.getData() + 1;
                cell.setData(num);
            } else {
                cell.setData(SkyscrapersType.UNKNOWN.toValue());
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() > 0) {
                    int num = cell.getData() - 1;
                    cell.setData(num);
                } else {
                    cell.setData(cell.getMax());
                }
            }
        }
    }
}
