package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class RippleEffectCellController extends ElementController {
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        RippleEffectCell cell = (RippleEffectCell)data;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isControlDown()) {

            }
            else {
            }
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
        }
    }
}