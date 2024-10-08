package edu.rpi.legup.puzzle.binary;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;

public class BinaryView extends GridBoardView {

    /**
     * Creates and arranges the visual components for each cell in the binary puzzle. Initializes
     * the view by setting up the board controller, binary controller, and the grid dimensions. For
     * each cell in the BinaryBoard, it creates a corresponding BinaryElementView, sets its index,
     * size, and location, and adds it to the list of element views to be displayed.
     *
     * @param board The BinaryBoard representing the current state of the binary puzzle
     */
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
