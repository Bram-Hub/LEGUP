package app;

import model.Puzzle;
import model.rules.Tree;
import model.rules.TreeElementType;
import ui.boardview.BoardView;
import ui.treeview.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static app.GameBoardFacade.getInstance;

public class TreeController extends Controller
{
    private TreeView treeView;
    private TreeElementView hover;

    /**
     * TreeController Constructor - creates a controller object to listen
     * to ui events from a TreePanel
     */
    public TreeController()
    {
        hover = null;
    }

    /**
     * Mouse Clicked event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        Tree tree = getInstance().getTree();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView treeNodeView = treeView.getTreeElementView(point);
        Puzzle puzzle = getInstance().getPuzzleModule();
        if(treeNodeView != null)
        {
            treeView.getTreeSelection().newSelection(treeNodeView);
            treeView.repaint();
            puzzle.setCurrentBoard(treeNodeView.getTreeElement().getBoard());
            getInstance().getLegupUI().repaintBoard();
            //System.err.println("Location: " + nodeView.getLocation());
        }
    }

    /**
     * Mouse Pressed event - sets the cursor to the move cursor and stores
     * info for possible panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
    }

    /**
     * Mouse Released event - sets the cursor back to the default cursor and reset
     * info for panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);

        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(e.isControlDown())
            {
                //((TreeView)viewer).toggleSelection();
            }
        }
    }

    /**
     * Mouse Entered event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        Tree tree = getInstance().getTree();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView treeNodeView = treeView.getTreeElementView(point);
        Puzzle puzzle = getInstance().getPuzzleModule();
        if(treeNodeView != null)
        {
            //treeView.repaint();
            puzzle.setCurrentBoard(treeNodeView.getTreeElement().getBoard());
            getInstance().getLegupUI().repaintBoard();
        }
    }

    /**
     * Mouse Exited event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        Tree tree = getInstance().getTree();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView treeNodeView = treeView.getTreeElementView(point);
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeSelection selection = treeView.getTreeSelection();
        if(treeNodeView != null)
        {
            //treeView.repaint();
            TreeElementView elementView = selection.getFirstSelection();
            puzzle.setCurrentBoard(elementView.getTreeElement().getBoard());
            getInstance().getLegupUI().repaintBoard();
        }
    }

    /**
     * Mouse Dragged event - adjusts the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
    }

    /**
     * Mouse Moved event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        TreeElementView treeNodeView = treeView.getTreeElementView(point);
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeSelection selection = treeView.getTreeSelection();
        if(treeNodeView != null)
        {
            //treeView.repaint();
            if(treeNodeView != hover)
            {
                puzzle.setCurrentBoard(treeNodeView.getTreeElement().getBoard());
                getInstance().getLegupUI().repaintBoard();
                hover = treeNodeView;
            }
        }
        else
        {
            puzzle.setCurrentBoard(selection.getFirstSelection().getTreeElement().getBoard());
            getInstance().getLegupUI().repaintBoard();
            hover = null;
        }
    }

    /**
     * Mouse Wheel Moved event - zooms in on the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        super.mouseWheelMoved(e);
    }

    private void addChildElement()
    {
        TreeView treeView = (TreeView)viewer;
        Tree tree = getInstance().getTree();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView elementView = treeView.getTreeSelection().getFirstSelection();
        Puzzle puzzle = getInstance().getPuzzleModule();
        if(elementView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView)elementView;

        }
        else
        {
            TreeTransitionView nodeView = (TreeTransitionView)elementView;

        }
    }
}
