package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.ElementController;
import java.awt.Color;
import java.awt.Dimension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A view class for a grid-based board that displays elements in a grid layout. This class extends
 * BoardView and is responsible for managing and rendering grid-based elements.
 */
public class GridBoardView extends BoardView {
    protected static final Logger LOGGER = LogManager.getLogger(GridBoardView.class.getName());

    protected Dimension gridSize;
    protected Dimension elementSize;

    /**
     * GridBoardView Constructor creates a GridBoardView object using the controller handle the ui
     * events
     *
     * @param boardController controller that handles the ui events
     * @param gridSize dimension of the grid
     * @param elementController controller that handles the ui events
     */
    public GridBoardView(
            BoardController boardController,
            ElementController elementController,
            Dimension gridSize) {
        this(boardController, elementController);
        this.gridSize = gridSize;
        this.elementSize = new Dimension(30, 30);
        initSize();
    }

    /**
     * GridBoardView Constructor creates a GridBoardView object using the controller handle the ui
     * events
     *
     * @param boardController controller that handles the ui events
     */
    private GridBoardView(BoardController boardController, ElementController elementController) {
        super(boardController, elementController);
        setBackground(new Color(0xE0E0E0));
    }

    /**
     * Gets the GridElementView from the puzzleElement index or null if out of bounds
     *
     * @param index index of the ElementView
     * @return GridElementView at the specified index
     */
    public GridElementView getElement(int index) {
        if (index < elementViews.size()) {
            return (GridElementView) elementViews.get(index);
        }
        return null;
    }

    /**
     * Retrieves the GridElementView at the specified grid coordinates (xIndex, yIndex). Returns
     * null if the coordinates are out of bounds.
     *
     * @param xIndex the x-coordinate (column) of the element view to retrieve
     * @param yIndex the y-coordinate (row) of the element view to retrieve
     * @return the GridElementView at the specified coordinates, or null if out of bounds
     */
    public GridElementView getElement(int xIndex, int yIndex) {
        if (xIndex < gridSize.width && yIndex < gridSize.height) {
            return (GridElementView) elementViews.get(yIndex * gridSize.width + xIndex);
        }
        return null;
    }

    /**
     * Initializes the initial dimension of the viewport for the GridBoardView. Sets the size of the
     * board view and adjusts the zoom to fit.
     */
    @Override
    public void initSize() {
        setSize(getProperSize());
        zoomFit();
    }

    /**
     * Determines the proper dimension of the grid view based on grid size and element size.
     *
     * @return the dimension of the grid view
     */
    protected Dimension getProperSize() {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = gridSize.width * elementSize.width;
        boardViewSize.height = gridSize.height * elementSize.height;
        return boardViewSize;
    }

    /**
     * Retrieves the selection popup menu for data selection. Currently returns null as there is no
     * implementation.
     *
     * @return null
     */
    public DataSelectionView getSelectionPopupMenu() {
        return null;
    }

    /**
     * Gets the size of each element in the grid
     *
     * @return the dimension of each element in the grid
     */
    public Dimension getElementSize() {
        return this.elementSize;
    }
}
