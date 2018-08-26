package edu.rpi.legup.controller;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.history.AddTreeElementCommand;
import edu.rpi.legup.ui.treeview.*;
import edu.rpi.legup.history.DeleteTreeElementCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.MergeCommand;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreeToolBarController implements ActionListener {
    private TreePanel treePanel;

    /**
     * TreeToolBarController Constructor creates a controller for clicking buttons on the toolbar
     *
     * @param treePanel tree panel associated with this controller
     */
    public TreeToolBarController(TreePanel treePanel) {
        this.treePanel = treePanel;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        TreeToolBarButton button = (TreeToolBarButton) e.getSource();
        if (button.getToolBarName() == TreeToolBarName.ADD_CHILD) {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            AddTreeElementCommand add = new AddTreeElementCommand(selection);
            if (add.canExecute()) {
                add.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(add);
            } else {
                treePanel.updateError(add.getError());
            }
        } else if (button.getToolBarName() == TreeToolBarName.DEL_CHILD) {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            DeleteTreeElementCommand del = new DeleteTreeElementCommand(selection);
            if (del.canExecute()) {
                del.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(del);
            } else {
                treePanel.updateError(del.getError());
            }
        } else if (button.getToolBarName() == TreeToolBarName.MERGE) {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();

            ICommand merge = new MergeCommand(selection);
            if (merge.canExecute()) {
                merge.execute();
                GameBoardFacade.getInstance().getHistory().pushChange(merge);
            } else {
                treePanel.updateError(merge.getError());
            }
        } else if (button.getToolBarName() == TreeToolBarName.COLLAPSE) {
            TreeViewSelection selection = treePanel.getTreeView().getSelection();
            for (TreeElementView view : selection.getSelectedViews()) {
                view.setCollapsed(!view.isCollapsed());
            }
        }
    }
}
