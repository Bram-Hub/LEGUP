package app;

import model.rules.Tree;
import model.rules.TreeNode;
import ui.boardview.BoardView;
import ui.treeview.TreeNodeView;
import ui.treeview.TreeView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static app.GameBoardFacade.getInstance;

public class TreeController extends Controller
{
    private TreeView treeView;

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
        TreeView treeView = (TreeView)viewer;
        Point point = treeView.getActualPoint(e.getPoint());
        Tree tree = getInstance().getTree();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeNodeView nodeView = treeView.getTreeNodeView(point);
        if(nodeView != null)
        {
            tree.newTreeNodeSelection(nodeView.getTreeNode());
            treeView.repaint();
            getInstance().getLegupUI().repaintBoard();
            System.err.println("Location: " + nodeView.getLocation());
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

    }

    /**
     * Mouse Exited event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

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
}
