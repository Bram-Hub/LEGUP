package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class BattleShipView extends GridBoardView {
    public BattleShipView(BattleShipBoard board) {
        super(new BoardController(), new BattleShipCellController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BattleShipCell cell = (BattleShipCell) puzzleElement;
            Point loc = cell.getLocation();
            BattleShipElementView elementView = new BattleShipElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);
    }
}