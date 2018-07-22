package ui.boardview;

import controller.BoardController;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.CaseBoard;
import model.gameboard.ElementData;
import model.observer.IBoardListener;
import ui.DynamicViewer;

import java.awt.*;
import java.util.ArrayList;

public abstract class BoardView extends DynamicViewer implements IBoardListener
{
    protected Board board;
    protected ArrayList<ElementView> elementViews;
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
        this.board = null;
        this.elementViews = new ArrayList<>();
        this.elementController = elementController;
        this.selection = new ElementSelection();

        elementController.setBoardView(this);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);
        getViewport().addKeyListener(elementController);
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
     * Gets the ElementView from the element index or
     * null if out of bounds
     *
     * @param index index of the ElementView
     * @return ElementView at the specified index
     */
    public abstract ElementView getElement(int index);

    /**
     * Sets the ElementView list
     *
     * @param elements ElementView list
     */
    public void setElementViews(ArrayList<ElementView> elements)
    {
        elementViews = elements;
    }

    /**
     * Gets the ElementView from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return ElementView at the specified location
     */
    public abstract ElementView getElement(Point point);

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
     * Gets the board associated with this view
     *
     * @return board
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Sets the board associated with this view
     *
     * @param board board
     */
    public void setBoard(Board board)
    {
        if(this.board != board)
        {
            this.board = board;

            if(board instanceof CaseBoard)
            {
                CaseBoard caseBoard = (CaseBoard)board;
                Board baseBoard = caseBoard.getBaseBoard();

                for(ElementView elementView: elementViews)
                {
                    ElementData element = baseBoard.getElementData(elementView.getIndex());
                    elementView.setElement(element);
                    elementView.setShowCasePicker(true);
                    elementView.setCaseRulePickable(caseBoard.isPickable(elementView.getElement()));
                }
            }
            else
            {
                for(ElementView elementView: elementViews)
                {
                    elementView.setElement(board.getElementData(elementView.getIndex()));
                    elementView.setShowCasePicker(false);
                }
            }
        }
    }

    /**
     * Board element has changed
     *
     * @param board board to update the BoardView
     */
    public void onBoardChanged(Board board)
    {
        setBoard(board);
        repaint();
    }

    /**
     * Gets the amount of puzzle elements for this board
     *
     * @return the amount of puzzle elements for this board
     */
    public int getElementCount()
    {
        return elementViews.size();
    }

    /**
     * Gets the PuzzleElements associated with the BoardView
     *
     * @return list of PuzzleElements
     */
    public ArrayList<ElementView> getElementViews()
    {
        return elementViews;
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
        for(ElementView element: elementViews)
        {
            element.draw(graphics2D);
        }
    }

    /**
     * Called when the board element changed
     *
     * @param data element of the element that changed
     */
    @Override
    public void onBoardDataChanged(ElementData data)
    {
        repaint();
    }

    public abstract DataSelectionView getSelectionPopupMenu();
}
