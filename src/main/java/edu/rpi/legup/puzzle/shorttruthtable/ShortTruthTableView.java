package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;

public class ShortTruthTableView extends GridBoardView {

    public ShortTruthTableView(ShortTruthTableBoard board) {
        super(new BoardController(), new ShortTruthTableController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            ShortTruthTableCell cell = (ShortTruthTableCell) puzzleElement;
            //            System.out.println("STTView :"+cell);
            Point loc = cell.getLocation();
            ShortTruthTableElementView elementView = new ShortTruthTableElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
