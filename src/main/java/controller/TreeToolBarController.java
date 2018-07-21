package controller;

import app.GameBoardFacade;
import history.AddTreeElementCommand;
import ui.treeview.*;
import history.DeleteTreeElementCommand;
import history.ICommand;
import history.MergeCommand;

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
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            AddTreeElementCommand add = new AddTreeElementCommand(selection);
            if(add.canExecute())
            {
                add.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(add);
            }
            else
            {
                treePanel.updateError(add.getExecutionError());
            }
        }
        else if(button.getToolBarName() == TreeToolBarName.DEL_CHILD)
        {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            DeleteTreeElementCommand del = new DeleteTreeElementCommand(selection);
            if(del.canExecute())
            {
                del.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(del);
            }
            else
            {
                treePanel.updateError(del.getExecutionError());
            }
        }
        else if(button.getToolBarName() == TreeToolBarName.MERGE)
        {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            ICommand merge = new MergeCommand(selection);
            if(merge.canExecute())
            {
                merge.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(merge);
            }
            else
            {
                treePanel.updateError(merge.getExecutionError());
            }
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
