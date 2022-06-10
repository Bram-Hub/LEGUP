package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class NurikabeView extends GridBoardView {

    public NurikabeView(NurikabeBoard board) {
        super(new BoardController(), new NurikabeController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            NurikabeCell cell = (NurikabeCell) puzzleElement;
            Point loc = cell.getLocation();
            NurikabeElementView elementView = new NurikabeElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
