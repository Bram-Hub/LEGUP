package edu.rpi.legup.controller;

import edu.rpi.legup.ui.treeview.TreeNodeView;
import edu.rpi.legup.ui.treeview.TreeView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SelectionController implements MouseListener, MouseMotionListener
{
    private TreeNodeView treeNodeView;
    private TreeView treeView;

    /**
     * SelectionController Constructor - creates a mouse listener
     * for a tree node puzzleElement
     *
     * @param treeNodeView tree node view
     * @param treeView tree view
     */
    public SelectionController(TreeNodeView treeNodeView, TreeView treeView)
    {
        this.treeNodeView = treeNodeView;
        this.treeView = treeView;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(e.isControlDown())
        {
//            GameBoardFacade.getInstance().getTree().toggleTreeNodeSelection(treeNodeView.getTreeElement());
        }
        else
        {
//            GameBoardFacade.getInstance().getTree().newTreeNodeSelection(treeNodeView.getTreeElement());
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
        treeView.setNodeHover(treeNodeView);
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
