package app;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.Tree;
import model.rules.TreeNode;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static app.GameBoardFacade.*;

public class ElementController implements MouseListener, MouseMotionListener
{
    private BoardView boardView;
    private PuzzleElement hover;

    /**
     * ElementController - creates and element controller to handles ui events
     * associated interacting with a PuzzleElement
     *
     * @param boardView
     */
    public ElementController(BoardView boardView)
    {
        this.boardView = boardView;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        Tree tree = getInstance().getTree();
        TreeNode node = tree.getFirstSelected();
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        if(element != null)
        {
            int index = element.getIndex();
            ElementData data = board.getElementData(index);
            if(!data.isModifiable() || (node == tree.getRootNode() && node.getChildren().size() > 0))
                return;
            if(node.getRule() != null || node == tree.getRootNode())
            {
                getInstance().getTree().addToSelected();
                getInstance().getLegupUI().repaintTree();
                board = getInstance().getBoard();
                data = board.getElementData(index);
            }

            if(e.getButton() == MouseEvent.BUTTON1)
                data.setValueInt((data.getValueInt() + 1) % 10);
            else if(e.getButton() == MouseEvent.BUTTON3)
                data.setValueInt((data.getValueInt() + 9) % 10);

            node.propagateChanges(index);

            getInstance().getLegupUI().repaintBoard();
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        if(element != null)
        {
            int index = element.getIndex();
            ElementData data = board.getElementData(index);

            //hover = element;
            element.setHover(true);

            getInstance().getLegupUI().repaintBoard();
        }
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        if(element != null)
        {
            int index = element.getIndex();

            ElementData data = board.getElementData(index);

            if(hover != null) {
                hover.setHover(false);
            }


            getInstance().getLegupUI().repaintBoard();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        if(element != null)
        {
            int index = element.getIndex();
            ElementData data = board.getElementData(index);
            if(hover != null)
                hover.setHover(false);
            hover = element;
            element.setHover(true);

            getInstance().getLegupUI().repaintBoard();
        }
    }
}
