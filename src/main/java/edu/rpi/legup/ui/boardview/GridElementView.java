package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.model.gameboard.GridCell;

/**
 * A view class for a grid cell element in the board.
 * This class extends ElementView and represents a specific type of element view
 * associated with a GridCell.
 */
public class GridElementView extends ElementView {
    public GridElementView(GridCell cell) {
        super(cell);
    }
}
