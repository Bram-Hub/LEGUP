package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class ThermometerView extends GridBoardView {

    public ThermometerView(ThermometerBoard board) {
        super(new BoardController(), new ThermometerController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            ThermometerCell cell = (ThermometerCell) puzzleElement;
            Point loc = cell.getLocation();
            ThermometerElementView elementView = new ThermometerElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}

