package ui.boardview;

import controller.BoardController;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.GridBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class GridBoardView extends BoardView
{
    protected Dimension gridSize;
    protected Dimension elementSize;

    /**
     * GridBoardView Constructor - creates a GridBoardView object using
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
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
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
     */
    private GridBoardView(BoardController boardController, ElementController elementController)
    {
        super(boardController, elementController);
        setBackground( new Color(0xE0E0E0) );
    }

    /**
     * Gets the GridElement from the element index or
     * null if out of bounds
     *
     * @param index index of the PuzzleElement
     * @return GridElement at the specified index
     */
    public GridElement getElement(int index)
    {
        if(index < puzzleElements.size())
        {
            return (GridElement)puzzleElements.get(index);
        }
        return null;
    }

    /**
     * Gets the GridElement from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return GridElement at the specified location
     */
    public PuzzleElement getElement(Point point)
    {
        Point scaledPoint = new Point((int)Math.round(point.x / getScale()), (int)Math.round(point.y / getScale()));
        for(PuzzleElement element: puzzleElements)
        {
            if(element.isWithinBounds(scaledPoint))
            {
                return element;
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

    /**
     * Board Data changed
     *
     * @param board board to update the BoardView
     */
    public void updateBoard(Board board)
    {
        GridBoard gridBoard = (GridBoard)board;
        for(PuzzleElement element: puzzleElements)
        {
            element.setData(gridBoard.getElementData(element.getIndex()));
        }
        repaint();
    }

    public DataSelectionView getSelectionPopupMenu()
    {
        return null;
    }
}

