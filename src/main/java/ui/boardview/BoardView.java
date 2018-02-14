package ui.boardview;

import app.BoardController;
import model.gameboard.Board;
import ui.DynamicViewer;

import java.awt.*;
import java.util.ArrayList;

public abstract class BoardView extends DynamicViewer
{
    protected ArrayList<PuzzleElement> puzzleElements;

    /**
     * BoardView Constructor - creates a BoardView object using
     * the controller handle the ui events
     *
     * @param boardController controller that handles the ui events
     */
    public BoardView(BoardController boardController)
    {
        super(boardController);
        puzzleElements = new ArrayList<>();
    }

    /**
     * Initializes the initial dimension of the viewport for the BoardView
     */
    public abstract void initSize();

    /**
     * Gets the PuzzleElement from the element index or
     * null if out of bounds
     *
     * @param index index of the PuzzleElement
     * @return PuzzleElement at the specified index
     */
    public abstract PuzzleElement getElement(int index);

    /**
     * Gets the PuzzleElement from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return PuzzleElement at the specified location
     */
    public abstract PuzzleElement getElement(Point point);

    /**
     * Draws the board
     *
     * @param graphics2D graphics2D object used to draw
     */
    protected abstract void draw(Graphics2D graphics2D);

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
}
