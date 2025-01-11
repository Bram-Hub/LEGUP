package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class YinYangView extends GridBoardView {

    public YinYangView(YinYangBoard board) {
        super(new BoardController(), new YinYangController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) puzzleElement;
            Point loc = cell.getLocation();
            YinYangElementView elementView = new YinYangElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}