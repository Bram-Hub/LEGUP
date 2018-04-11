package controller;

import model.Puzzle;
import model.tree.Tree;
import model.tree.TreeElementType;
import ui.boardview.BoardView;
import ui.treeview.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static app.GameBoardFacade.getInstance;

public class TreeController extends Controller
{
    /**
     * TreeController Constructor - creates a controller object to listen
     * to ui events from a TreePanel
     */
    public TreeController()
    {

    }

    /**
     * Mouse Clicked event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

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

        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        TreeElementView elementView = treeView.getTreeElementView(point);
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeSelection selection = treeView.getTreeSelection();
        if(elementView != null)
        {
            if(e.isShiftDown())
            {
                selection.addToSelection(elementView);
            }
            else if(e.isControlDown())
            {
                selection.toggleSelection(elementView);
            }
            else
            {
                selection.newSelection(elementView);
            }
            treeView.repaint();
            puzzle.setCurrentBoard(elementView.getTreeElement().getBoard());
            getInstance().getLegupUI().repaintBoard();
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

        selection.setMousePoint(null);
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
        if(puzzle != null)
        {
            TreeSelection selection = treeView.getTreeSelection();
            selection.setMousePoint(treeView.getActualPoint(e.getPoint()));
            if(treeNodeView != null)
            {
                if(treeNodeView != selection.getHover())
                {
                    puzzle.setCurrentBoard(treeNodeView.getTreeElement().getBoard());
                    treeView.repaint();
                    getInstance().getLegupUI().repaintBoard();
                    selection.newHover(treeNodeView);
                }
                else
                {
                    treeView.repaint();
                }
            }
            else
            {
                puzzle.setCurrentBoard(selection.getFirstSelection().getTreeElement().getBoard());
                treeView.repaint();
                getInstance().getLegupUI().repaintBoard();
                selection.clearHover();
            }
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
//        TreeView treeView = (TreeView)viewer;
//        TreeSelection selection = treeView.getTreeSelection();
//        Point newPoint = new Point(e.getX() + treeView.getViewport().getX(), e.getY() + treeView.getViewport().getY());
//
//        PointerInfo a = MouseInfo.getPointerInfo();
//        Point b = a.getLocation();
//        SwingUtilities.convertPointFromScreen(b, treeView.getCanvas());
//
//        selection.setMousePoint(b);
    }
}
