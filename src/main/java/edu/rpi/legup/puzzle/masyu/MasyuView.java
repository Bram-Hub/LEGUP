package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MasyuView extends GridBoardView {
    private List<MasyuLineView> lineViews;

    public MasyuView(MasyuBoard board) {
        super(new BoardController(), new MasyuController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            MasyuCell cell = (MasyuCell) puzzleElement;
            Point loc = cell.getLocation();
            MasyuElementView elementView = new MasyuElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
        lineViews = new ArrayList<>();
        for (MasyuLine line : board.getLines()) {
            MasyuLineView lineView = new MasyuLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.drawBoard(graphics2D);
        lineViews.forEach(masyuLineView -> masyuLineView.draw(graphics2D));
    }

    /**
     * Called when the board puzzleElement changed
     *
     * @param puzzleElement puzzleElement of the puzzleElement that changed
     */
    @Override
    public void onBoardDataChanged(PuzzleElement puzzleElement) {
        if (puzzleElement instanceof MasyuLine) {
            MasyuLineView lineView = new MasyuLineView((MasyuLine) puzzleElement);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
        repaint();
    }
}
