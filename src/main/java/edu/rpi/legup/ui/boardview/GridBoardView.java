package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.ElementController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

public class GridBoardView extends BoardView
{
    protected Dimension gridSize;
    protected Dimension elementSize;

    /**
     * GridBoardView Constructor - creates a GridBoardView object using
     * the edu.rpi.legup.controller handle the edu.rpi.legup.ui events
     *
     * @param boardController edu.rpi.legup.controller that handles the edu.rpi.legup.ui events
     * @param gridSize dimension of the grid
     */
    public GridBoardView(BoardController boardController, ElementController elementController, Dimension gridSize)
    {
        this(boardController, elementController);
        this.gridSize = gridSize;
        this.elementSize = new Dimension(30,30);
        initSize();
    }

    /**
     * GridBoardView Constructor - creates a GridBoardView object using
     * the edu.rpi.legup.controller handle the edu.rpi.legup.ui events
     *
     * @param boardController edu.rpi.legup.controller that handles the edu.rpi.legup.ui events
     */
    private GridBoardView(BoardController boardController, ElementController elementController)
    {
        super(boardController, elementController);
        setBackground( new Color(0xE0E0E0) );
    }

    /**
     * Gets the GridElementView from the puzzleElement index or
     * null if out of bounds
     *
     * @param index index of the ElementView
     * @return GridElementView at the specified index
     */
    public GridElementView getElement(int index)
    {
        if(index < elementViews.size())
        {
            return (GridElementView) elementViews.get(index);
        }
        return null;
    }

    public GridElementView getElement(int xIndex, int yIndex) {
        if(xIndex < gridSize.width && yIndex < gridSize.height) {
            return (GridElementView) elementViews.get(yIndex * gridSize.width + xIndex);
        }
        return null;
    }

    /**
     * Gets the GridElementView from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return GridElementView at the specified location
     */
    public GridElementView getElement(Point point)
    {
        Point scaledPoint = new Point((int)Math.round(point.x / getScale()), (int)Math.round(point.y / getScale()));
        for(ElementView element: elementViews)
        {
            if(element.isWithinBounds(scaledPoint))
            {
                return (GridElementView) element;
            }
        }
        return null;
    }

    /**
     * Initializes the initial dimension of the viewport for the GridBoardView
     */
    @Override
    public void initSize()
    {
        setSize( getProperSize() );
        zoomFit();
    }

    /**
     * Helper method to determine the proper dimension of the grid view
     *
     * @return proper dimension of the grid view
     */
    protected Dimension getProperSize()
    {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = gridSize.width * elementSize.width;
        boardViewSize.height = gridSize.height * elementSize.height;
        return boardViewSize;
    }

    public DataSelectionView getSelectionPopupMenu()
    {
        return null;
    }

    public Dimension getElementSize() {
        return this.elementSize;
    }

}

