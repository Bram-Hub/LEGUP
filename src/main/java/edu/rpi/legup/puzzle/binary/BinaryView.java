package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;

public class BinaryView extends GridBoardView {

    public BinaryView(BinaryBoard board) {
        super(new BoardController(), new BinaryController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BinaryCell cell = (BinaryCell) puzzleElement;
            Point loc = cell.getLocation();
            BinaryElementView elementView = new BinaryElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
