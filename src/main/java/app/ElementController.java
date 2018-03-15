package app;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.boardview.BoardView;
import ui.boardview.DataSelectionView;
import ui.boardview.PuzzleElement;
import ui.boardview.SelectionItemView;
import ui.treeview.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

import static app.GameBoardFacade.*;

public class ElementController implements MouseListener, MouseMotionListener, ActionListener
{
    private BoardView boardView;
    private PuzzleElement hover;
    private ArrayList<PuzzleElement> selected;

    /**
     * ElementController - creates and element controller to handles ui events
     * associated interacting with a PuzzleElement
     *
     * @param boardView
     */
    public ElementController(BoardView boardView)
    {
        this.boardView = boardView;
        this.selected = new ArrayList<>();
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
            {
                if(e.isControlDown())
                {
                    boardView.getSelectionPopupMenu().show(view, boardView.getCanvas().getX() + e.getX(),boardView.getCanvas().getY() + e.getY());
                    selected.add(element);
                }
                else
                {
                    data.setValueInt((data.getValueInt() + 1) % 10);
                }
            }
            else if(e.getButton() == MouseEvent.BUTTON3)
            {
                data.setValueInt((data.getValueInt() + 9) % 10);
            }

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

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        System.err.println("aas");
        PuzzleElement sel = selected.get(0);
        ElementData data = sel.getData();
        int index = sel.getIndex();

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeTransitionView transitionView = (TreeTransitionView)selectedView;

        Board prevBord = transitionView.getTreeElement().getParentNode().getBoard();

        int value = ((SelectionItemView)e.getSource()).getData().getValueInt();

        data.setValueInt(value);

        if(data.getValueInt() != prevBord.getElementData(data.getIndex()).getValueInt())
            data.setModified(true);
        else
            data.setModified(false);

        transitionView.getTreeElement().propagateChanges(index);

        getInstance().getLegupUI().repaintBoard();
        selected.clear();
    }
}
