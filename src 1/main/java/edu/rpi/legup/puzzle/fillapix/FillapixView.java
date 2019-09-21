package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class FillapixView extends GridBoardView {
    public FillapixView(FillapixBoard board) {
        super(new BoardController(), new FillapixCellController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            FillapixCell cell = (FillapixCell) puzzleElement;
            Point loc = cell.getLocation();
            FillapixElementView elementView = new FillapixElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}