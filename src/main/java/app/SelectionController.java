package app;

import ui.treeview.TreeNodeElement;
import ui.treeview.TreeView;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SelectionController implements MouseListener, MouseMotionListener
{
    private TreeNodeElement treeNodeElement;
    private TreeView treeView;

    /**
     * SelectionController Constructor - creates a mouse listener
     * for a tree node element
     *
     * @param treeNodeElement tree node element
     * @param treeView tree view
     */
    public SelectionController(TreeNodeElement treeNodeElement, TreeView treeView)
    {
        this.treeNodeElement = treeNodeElement;
        this.treeView = treeView;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.isControlDown())
        {
            GameBoardFacade.getInstance().getTree().toggleTreeNodeSelection(treeNodeElement.getTreeNode());
        }
        else
        {
            GameBoardFacade.getInstance().getTree().newTreeNodeSelection(treeNodeElement.getTreeNode());
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        treeView.setNodeHover(treeNodeElement);
        treeView.invalidate();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        treeView.setNodeHover(null);
        treeView.invalidate();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
}
