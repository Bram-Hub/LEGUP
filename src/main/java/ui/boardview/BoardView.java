package ui.boardview;

import app.BoardController;
import app.ElementController;
import model.Puzzle;
import model.gameboard.Board;
import puzzles.sudoku.SudokuCell;
import puzzles.sudoku.SudokuElement;
import ui.DynamicViewer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class BoardView extends DynamicViewer
{
    protected ArrayList<PuzzleElement> puzzleElements;
    protected ElementController elementController;

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
        this.elementController = new ElementController(this);
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

    public abstract DataSelectionView getSelectionPopupMenu();
}
