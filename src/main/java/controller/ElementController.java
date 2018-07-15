package controller;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.observer.ITreeListener;
import model.rules.CaseRule;
import model.tree.Tree;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.boardview.BoardView;
import ui.boardview.ElementSelection;
import ui.boardview.PuzzleElement;
import ui.boardview.SelectionItemView;
import ui.treeview.*;
import history.ICommand;
import history.EditDataCommand;

import java.awt.event.*;
import java.util.ArrayList;

import static app.GameBoardFacade.*;

public class ElementController implements MouseListener, MouseMotionListener, ActionListener, KeyListener
{
    protected BoardView boardView;

    /**
     * ElementController - element controller to handles ui events
     * associated interacting with a PuzzleElement
     */
    public ElementController()
    {
        this.boardView = null;
    }

    public void setBoardView(BoardView boardView)
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
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement elementView = boardView.getElement(e.getPoint());
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        if(elementView != null)
        {
            if(board.getCaseRule() != null)
            {
                if(selectedView.getType() == TreeElementType.NODE)
                {
                    TreeNodeView nodeView = (TreeNodeView)selectedView;
                    TreeNode node = nodeView.getTreeElement();
                    CaseRule caseRule = board.getCaseRule();
                    ArrayList<Board> cases = caseRule.getCases(board, elementView.getIndex());
                    for(Board b: cases)
                    {
                        TreeTransition transition = tree.addNewTransition(node);
                        b.setModifiable(false);
                        transition.setBoard(b);
                        transition.setRule(caseRule);

                        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(transition));
                        //TreeTransitionView transitionView = treeView.addTransitionView(nodeView, transition);

                        TreeNode n = tree.addNode(transition);
                        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(n));
                        //treeView.addNodeView(transitionView, n);
                    }
                    selection.newSelection(nodeView.getChildrenViews().get(0).getChildView());
                    getInstance().getPuzzleModule().setCurrentBoard(node.getChildren().get(0).getBoard());
                    getInstance().getLegupUI().repaintTree();
                }
            }
            else
            {
                ICommand edit = new EditDataCommand(elementView, selectedView, e);
                if(edit.canExecute())
                {
                    edit.execute();
                    getInstance().getHistory().pushChange(edit);
                }
            }
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
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = boardView.getElement(e.getPoint());
        ElementSelection selection = boardView.getSelection();
        if(element != null)
        {
            selection.newHover(element);

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
        BoardView view = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = view.getElement(e.getPoint());
        if(element != null)
        {
            view.getSelection().clearHover();
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
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement element = boardView.getElement(e.getPoint());
        ElementSelection selection = boardView.getSelection();
        if(element != null && element != selection.getHover())
        {
            selection.newHover(element);
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
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement selectedElement = boardView.getSelection().getFirstSelection();
        ElementData data = selectedElement.getData();
        int index = selectedElement.getIndex();

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeTransitionView transitionView = (TreeTransitionView)selectedView;

        Board prevBord = transitionView.getTreeElement().getParents().get(0).getBoard();

        int value = ((SelectionItemView)e.getSource()).getData().getValueInt();

        data.setValueInt(value);

        if(data.getValueInt() != prevBord.getElementData(data.getIndex()).getValueInt())
            data.setModified(true);
        else
            data.setModified(false);

        transitionView.getTreeElement().propagateChanges(data);

        getInstance().getLegupUI().repaintBoard();
        boardView.getSelection().clearSelection();
    }

    public void changeCell(MouseEvent e, ElementData data)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(e.isControlDown() && this.boardView.getSelectionPopupMenu() != null)
            {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
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
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e)
    {

        System.err.println("Key pressed: " + e.getKeyCode());
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e)
    {

        System.err.println("Key pressed: " + e.getKeyCode());
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e)
    {
        Board board = getInstance().getBoard();
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        System.err.println("Key pressed: " + e.getKeyCode());
        if(board.getCaseRule() != null)
        {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                GameBoardFacade.getInstance().setBoard(selectedView.getTreeElement().getBoard());
            }
        }
    }
}
