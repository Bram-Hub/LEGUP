package app;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.rules.*;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;
import ui.treeview.*;

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
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        if(element != null)
        {
            int index = element.getIndex();
            ElementData data = board.getElementData(index);
            if(!data.isModifiable())
                return;
            if(selectedView.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)selectedView;
                TreeNode treeNode = (TreeNode)selectedView.getTreeElement();
                if(treeNode.getChildren().size() > 0)
                {
                    return;
                }
                TreeTransition transition = tree.addNewTransition(treeNode);
                TreeTransitionView transitionView = treeView.addNewTransitionView(nodeView, transition);

                selection.newSelection(transitionView);
                selectedView = transitionView;
                getInstance().getLegupUI().repaintTree();
                board = transition.getBoard();
                getInstance().getPuzzleModule().setCurrentBoard(board);
                data = board.getElementData(index);
            }

            TreeTransitionView transitionView = (TreeTransitionView) selectedView;
            if(e.getButton() == MouseEvent.BUTTON1)
                data.setValueInt((data.getValueInt() + 1) % 10);
            else if(e.getButton() == MouseEvent.BUTTON3)
                data.setValueInt((data.getValueInt() + 9) % 10);

            Board prevBord = transitionView.getTreeElement().getParentNode().getBoard();

            if(data.getValueInt() != prevBord.getElementData(data.getIndex()).getValueInt())
                data.setModified(true);
            else
                data.setModified(false);

            transitionView.getTreeElement().propagateChanges(index);

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
