package edu.rpi.legup.ui.boardview;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.observer.IBoardListener;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.ui.ScrollView;
import java.awt.*;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class representing a view for a board in the puzzle game. It handles the visual
 * representation and user interactions with the board elements.
 */
public abstract class BoardView extends ScrollView implements IBoardListener {
    protected TreeElement treeElement;
    protected Board board;
    protected ArrayList<ElementView> elementViews;
    protected ElementController elementController;
    protected ElementSelection selection;

    /**
     * BoardView Constructor creates a view for the board object using the controller handle the ui
     * events
     *
     * @param boardController controller that handles the ui events
     * @param elementController controller that handles the ui events
     */
    public BoardView(
            @NotNull BoardController boardController,
            @NotNull ElementController elementController) {
        super(boardController);
        this.treeElement = null;
        this.board = null;
        this.elementViews = new ArrayList<>();
        this.elementController = elementController;
        this.selection = new ElementSelection();

        elementController.setBoardView(this);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);
        addKeyListener(elementController);
    }

    /** Initializes the initial dimension of the viewport for the BoardView */
    public abstract void initSize();

    /**
     * Gets the dimension of the board view
     *
     * @return dimension of the board view
     */
    @NotNull protected abstract Dimension getProperSize();

    /**
     * Gets the ElementView from the puzzleElement index or null if out of bounds
     *
     * @param index index of the ElementView
     * @return ElementView at the specified index
     */
    @Nullable public abstract ElementView getElement(int index);

    /**
     * Sets the ElementView list
     *
     * @param elements ElementView list
     */
    public void setElementViews(@NotNull ArrayList<ElementView> elements) {
        elementViews = elements;
    }

    /**
     * Gets the ElementView from the location specified or null if one does not exist at that
     * location
     *
     * @param point location on the viewport
     * @return ElementView at the specified location, or null if none exists
     */
    @Nullable public ElementView getElement(@NotNull Point point) {
        Point scaledPoint =
                new Point(
                        (int) Math.round(point.x / getScale()),
                        (int) Math.round(point.y / getScale()));
        for (ElementView element : elementViews) {
            if (element.isWithinBounds(scaledPoint)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Gets the ElementSelection for this BoardView
     *
     * @return the ElementSelection
     */
    @NotNull public ElementSelection getSelection() {
        return selection;
    }

    /**
     * Gets the board associated with this view
     *
     * @return board
     */
    @Nullable public Board getBoard() {
        return board;
    }

    /**
     * Sets the board associated with this view
     *
     * @param board board
     */
    public void setBoard(@NotNull Board board) {
        if (this.board != board) {
            this.board = board;

            if (board instanceof CaseBoard) {
                setCasePickable();
            } else {
                for (ElementView elementView : elementViews) {
                    elementView.setPuzzleElement(
                            board.getPuzzleElement(elementView.getPuzzleElement()));
                    elementView.setShowCasePicker(false);
                }
            }
        }
    }

    /** Configures the view to handle case interactions */
    protected void setCasePickable() {
        CaseBoard caseBoard = (CaseBoard) board;
        Board baseBoard = caseBoard.getBaseBoard();

        for (ElementView elementView : elementViews) {
            PuzzleElement puzzleElement =
                    baseBoard.getPuzzleElement(elementView.getPuzzleElement());
            elementView.setPuzzleElement(puzzleElement);
            elementView.setShowCasePicker(true);
            elementView.setCaseRulePickable(caseBoard.isPickable(puzzleElement, null));
        }
    }

    /**
     * Called when the tree element has changed.
     *
     * @param treeElement tree element
     */
    @Override
    public void onTreeElementChanged(@NotNull TreeElement treeElement) {
        this.treeElement = treeElement;
        setBoard(treeElement.getBoard());
        repaint();
    }

    /**
     * Called when the a case board has been added to the view.
     *
     * @param caseBoard case board to be added
     */
    @Override
    public void onCaseBoardAdded(@NotNull CaseBoard caseBoard) {
        setBoard(caseBoard);
        repaint();
    }

    /**
     * Gets the current tree element
     *
     * @return the current TreeElement, or null if none is set
     */
    @Nullable public TreeElement getTreeElement() {
        return this.treeElement;
    }

    /**
     * Gets the amount of edu.rpi.legup.puzzle elements for this board
     *
     * @return the amount of edu.rpi.legup.puzzle elements for this board
     */
    public int getElementCount() {
        return elementViews.size();
    }

    /**
     * Gets the ElementViews associated with the BoardView
     *
     * @return list of ElementViews
     */
    @NotNull public ArrayList<ElementView> getElementViews() {
        return elementViews;
    }

    /**
     * Gets the ElementController associated with this board view.
     *
     * @return the ElementController
     */
    @NotNull public ElementController getElementController() {
        return elementController;
    }

    @Override
    public void draw(@NotNull Graphics2D graphics2D) {
        drawBoard(graphics2D);
    }

    /**
     * Draws the board and its elements.
     *
     * @param graphics2D the Graphics2D context used for drawing
     */
    public void drawBoard(@NotNull Graphics2D graphics2D) {
        for (ElementView element : elementViews) {
            element.draw(graphics2D);
        }
    }

    /**
     * Called when the board puzzleElement changed
     *
     * @param puzzleElement puzzleElement of the puzzleElement that changed
     */
    @Override
    public void onBoardDataChanged(@NotNull PuzzleElement puzzleElement) {
        repaint();
    }

    /**
     * Gets the selection popup menu for this board view.
     *
     * @return the DataSelectionView associated with this view
     */
    @NotNull public abstract DataSelectionView getSelectionPopupMenu();
}
