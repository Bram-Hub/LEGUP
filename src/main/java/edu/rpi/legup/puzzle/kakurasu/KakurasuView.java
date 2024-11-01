package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class KakurasuView extends GridBoardView {

    public KakurasuView(KakurasuBoard board) {
        super(new BoardController(), new KakurasuController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            KakurasuCell cell = (KakurasuCell) puzzleElement;
            Point loc = cell.getLocation();
            KakurasuElementView elementView = new KakurasuElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}