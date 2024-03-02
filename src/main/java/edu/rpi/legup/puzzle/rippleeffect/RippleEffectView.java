package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.puzzle.lightup.LightUpCellController;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class RippleEffectView extends GridBoardView {

    public RippleEffectView(RippleEffectBoard board) {
        super(new BoardController(), new RippleEffectCellController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            RippleEffectCell cell = (RippleEffectCell) puzzleElement;
            Point loc = cell.getLocation();
            RippleEffectElementView elementView = new RippleEffectElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}