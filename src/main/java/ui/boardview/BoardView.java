package ui.boardview;

import controller.BoardController;
import controller.ElementController;
import model.gameboard.Board;
import ui.DynamicViewer;

import java.awt.*;
import java.util.ArrayList;

public abstract class BoardView extends DynamicViewer
{
    protected ArrayList<PuzzleElement> puzzleElements;
    protected ElementController elementController;
    protected ElementSelection selection;

    /**
     * BoardView Constructor - creates a BoardView object using
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
     */
    public BoardView(BoardController boardController, ElementController elementController)
    {
        super(boardController);
        this.puzzleElements = new ArrayList<>();
        this.elementController = elementController;
        this.selection = new ElementSelection();

        elementController.setBoardView(this);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);
    }

    /**
     * Initializes the initial dimension of the viewport for the BoardView
     */
    public abstract void initSize();

    /**
     * Gets the dimension of the board view
     *
     * @return dimension of the board view
     */
    protected abstract Dimension getProperSize();

    /**
     * Gets the PuzzleElement from the element index or
     * null if out of bounds
     *
     * @param index index of the PuzzleElement
     * @return PuzzleElement at the specified index
     */
    public abstract PuzzleElement getElement(int index);

    /**
     * Sets the PuzzleElement list
     *
     * @param elements PuzzleElement list
     */
    public void setPuzzleElements(ArrayList<PuzzleElement> elements)
    {
        puzzleElements = elements;
    }

    /**
     * Gets the PuzzleElement from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return PuzzleElement at the specified location
     */
    public abstract PuzzleElement getElement(Point point);

    /**
     * Gets the ElementSelection for this BoardView
     *
     * @return the ElementSelection
     */
    public ElementSelection getSelection()
    {
        return selection;
    }

    /**
     * Board data has changed
     *
     * @param board board to update the BoardView
     */
    public abstract void updateBoard(Board board);

    /**
     * Gets the amount of puzzle elements for this board
     *
     * @return the amount of puzzle elements for this board
     */
    public int getElementCount()
    {
        return puzzleElements.size();
    }

    /**
     * Gets the PuzzleElements associated with the BoardView
     *
     * @return list of PuzzleElements
     */
    public ArrayList<PuzzleElement> getPuzzleElements()
    {
        return puzzleElements;
    }

    public ElementController getElementController()
    {
        return elementController;
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        drawBoard(graphics2D);
    }

    public void drawBoard(Graphics2D graphics2D)
    {
        for(PuzzleElement element: puzzleElements)
        {
            element.draw(graphics2D);
        }
    }

    public abstract DataSelectionView getSelectionPopupMenu();
}
