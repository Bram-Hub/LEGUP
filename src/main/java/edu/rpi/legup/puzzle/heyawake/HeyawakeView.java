package edu.rpi.legup.puzzle.heyawake;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;
import java.awt.geom.Area;
import java.util.HashMap;
import java.util.Map;

public class HeyawakeView extends GridBoardView {

    private Map<Integer, Area> regionsBoundaries;

    public HeyawakeView(HeyawakeBoard board) {
        super(new BoardController(), new HeyawakeController(), board.getDimension());

        this.regionsBoundaries = new HashMap<>();

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            HeyawakeCell cell = (HeyawakeCell) puzzleElement;
            Point loc = cell.getLocation();
            HeyawakeElementView elementView = new HeyawakeElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);

            int regionIndex = cell.getRegionIndex();
            if (regionsBoundaries.get(regionIndex) == null) {
                regionsBoundaries.put(regionIndex, new Area(elementView.getBounds()));
            } else {
                regionsBoundaries.get(regionIndex).add(new Area(elementView.getBounds()));
            }
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        HeyawakeBoard heyawakeBoard = (HeyawakeBoard) board;

        for (ElementView elementView : elementViews) {
            elementView.draw(graphics2D);
        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(3));
        for (Area s : regionsBoundaries.values()) {
            graphics2D.draw(s);
        }
    }
}
