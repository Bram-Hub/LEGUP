package edu.rpi.legup.puzzle.minesweeper;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class MinesweeperView extends GridBoardView {

    public MinesweeperView(@NotNull MinesweeperBoard board) {
        super(new BoardController(), new MinesweeperController(), board.getDimension());

        for (PuzzleElement<?> puzzleElement : board.getPuzzleElements()) {
            final MinesweeperCell cell = (MinesweeperCell) puzzleElement;
            final Point loc = cell.getLocation();
            final MinesweeperElementView elementView = new MinesweeperElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}
