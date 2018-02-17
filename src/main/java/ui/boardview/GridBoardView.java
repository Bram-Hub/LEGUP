package ui.boardview;

import app.BoardController;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.gameboard.GridBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;

public class GridBoardView extends BoardView
{
    protected Dimension gridDimension;
    protected Dimension elementDimension;

    /**
     * GridBoardView Constructor - creates a GridBoardView object using
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
     * @param gridDimension dimension of the grid
     * @param elementDimension dimension of the elements
     */
    public GridBoardView(BoardController boardController, Dimension gridDimension, Dimension elementDimension)
    {
        this(boardController);
        this.gridDimension = gridDimension;
        this.elementDimension = elementDimension;
        setBackground( new Color(0xE0E0E0) );
        initSize();
    }

    /**
     * GridBoardView Constructor - creates a GridBoardView object using
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
     */
    private GridBoardView(BoardController boardController)
    {
        super(boardController);
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
    public GridElement getElement(Point point)
    {
        for(PuzzleElement element: puzzleElements)
        {
            if(element.isWithinBounds(point))
            {
                return (GridElement) element;
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
     * Helper method to determine the proper size of the grid view
     *
     * @return proper size of the grid view
     */
    private Dimension getProperSize()
    {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = gridDimension.width * (elementDimension.width + 2);
        boardViewSize.height = gridDimension.height * (elementDimension.height + 2);
        return boardViewSize;
    }

    /**
     * Draws the GridBoardView on the screen
     *
     * @param graphics2D graphics2D object used to draw
     */
    protected void draw(Graphics2D graphics2D)
    {
        for(PuzzleElement element: puzzleElements)
        {
            element.draw(graphics2D);
        }
    }

    /**
     * Board Data changed
     *
     * @param board board to update the BoardView
     */
    public void updateBoard(Board board)
    {
        GridBoard gridBoard = (GridBoard)board;
        puzzleElements.clear();
        for(int i = 0; i < gridBoard.getWidth(); i++)
        {
            for(int k = 0; k < gridBoard.getHeight(); k++)
            {
                GridElement element = new GridElement(gridBoard.getCell(i, k));
                element.cell = gridBoard.getCell(i, k);
                element.size = elementDimension;
                puzzleElements.add(element);
            }
        }
        revalidate();
        repaint();
    }
}

