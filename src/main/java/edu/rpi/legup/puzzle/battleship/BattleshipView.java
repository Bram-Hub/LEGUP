package edu.rpi.legup.puzzle.battleship;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;

/** The view for the Battleship puzzle */
public class BattleshipView extends GridBoardView {

    /**
     * Creates a new BattleshipView
     *
     * @param board the BattleshipBoard to display
     */
    public BattleshipView(BattleshipBoard board) {
        super(new BoardController(), new BattleshipCellController(), board.getDimension());

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            BattleshipCell cell = (BattleshipCell) puzzleElement;
            Point loc = cell.getLocation();
            BattleshipElementView elementView = new BattleshipElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);
    }
}
