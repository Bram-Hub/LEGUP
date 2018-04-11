package controller;

import app.GameBoardFacade;
import model.gameboard.Board;
import model.gameboard.ElementData;
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
import utility.ICommand;
import utility.EditDataCommand;

import java.awt.event.*;
import java.util.ArrayList;

import static app.GameBoardFacade.*;

public class ElementController implements MouseListener, MouseMotionListener, ActionListener
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
                        transition.setBoard(b);
                        transition.setRule(caseRule);
                        TreeTransitionView transitionView = treeView.addNewTransitionView(nodeView, transition);

                        tree.addNode(transition);
                        TreeNodeView newNodeView = transitionView.getChildView();
                        newNodeView.setTreeElement(transition.getChildNode());
                        newNodeView.setVisible(true);
                    }
                    selection.newSelection(nodeView.getChildrenViews().get(0).getChildView());
                    getInstance().getPuzzleModule().setCurrentBoard(node.getChildren().get(0).getBoard());
                    getInstance().getLegupUI().repaintTree();
                }
            }
            else
            {
                int index = elementView.getIndex();
                ElementData data = board.getElementData(index);
                if(!data.isModifiable())
                    return;

                ICommand edit = new EditDataCommand(elementView, selectedView, e);
                getInstance().getHistory().pushChange(edit);
                edit.execute();
                /*
                if(selectedView.getType() == TreeElementType.NODE)
                {
                    TreeNodeView nodeView = (TreeNodeView) selectedView;
                    TreeNode treeNode = (TreeNode) selectedView.getTreeElement();
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

                if(e.getButton() == MouseEvent.BUTTON1 && e.isControlDown())
                {
                    boardView.getSelection().newSelection(elementView);
                }

                changeCell(e, data);

                Board prevBord = transitionView.getTreeElement().getParentNode().getBoard();

                if(data.equals(prevBord.getElementData(data.getIndex())))
                {
                    data.setModified(false);
                    board.removeModifiedData(data);
                }
                else
                {
                    data.setModified(true);
                    board.addModifiedData(data);
                }

                transitionView.getTreeElement().propagateChanges(data);
                */
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

        Board prevBord = transitionView.getTreeElement().getParentNode().getBoard();

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
}
