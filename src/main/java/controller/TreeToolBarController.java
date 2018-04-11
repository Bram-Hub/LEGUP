package controller;

import app.GameBoardFacade;
import model.tree.Tree;
import model.tree.TreeElement;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import ui.treeview.*;
import utility.DeleteTreeElementCommand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeToolBarController implements ActionListener
{
    private TreePanel treePanel;

    public TreeToolBarController(TreePanel treePanel)
    {
        this.treePanel = treePanel;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        TreeToolBarButton button = (TreeToolBarButton)e.getSource();
        if(button.getToolBarName() == TreeToolBarName.ADD_CHILD)
        {

        }
        else if(button.getToolBarName() == TreeToolBarName.DEL_CHILD)
        {
            TreeSelection selection = treePanel.getTreeView().getTreeSelection();

            DeleteTreeElementCommand del = new DeleteTreeElementCommand(selection.getSelection());
            if(del.canExecute())
            {
                del.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(del);
            }
            else
            {
                treePanel.updateStatus(del.getExecutionError());
            }
        }
        else if(button.getToolBarName() == TreeToolBarName.MERGE)
        {
            //treePanel.mergeStates();
        }
        else if(button.getToolBarName() == TreeToolBarName.COLLAPSE)
        {
            //treePanel.collapseStates();
        }
        else if(button.getToolBarName() == TreeToolBarName.ZOOM_IN)
        {
            treePanel.getTreeView().zoomIn();
        }
        else if(button.getToolBarName() == TreeToolBarName.ZOOM_OUT)
        {
            treePanel.getTreeView().zoomOut();
        }
        else if(button.getToolBarName() == TreeToolBarName.ZOOM_RESET)
        {
            treePanel.getTreeView().zoomReset();
        }
        else if(button.getToolBarName() == TreeToolBarName.ZOOM_FIT)
        {
            treePanel.getTreeView().zoomFit();
        }
    }
}
