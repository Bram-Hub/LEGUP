package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.event.MouseEvent;

public class RippleEffectCellController extends ElementController {

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        // Since we don't need to change any cell data in Ripple Effect,
        // we leave this method empty.
    }
}