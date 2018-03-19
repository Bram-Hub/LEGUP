package controller;

import app.GameBoardFacade;
import model.tree.Tree;
import model.tree.TreeElementType;
import ui.treeview.*;

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
            Tree tree = GameBoardFacade.getInstance().getTree();
            TreeSelection selection = treePanel.getTreeView().getTreeSelection();

            TreeElementView selectedView = selection.getFirstSelection();
            TreeElementView newSelectedView = null;

            if(selectedView.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)selectedView;
                if(nodeView.getTreeElement().isRoot())
                {
                    return;
                }
                newSelectedView = nodeView.getParentViews().get(0);
            }
            else
            {
                TreeTransitionView transitionView = (TreeTransitionView)selectedView;
                newSelectedView = transitionView.getParentView();
            }

            treePanel.getTreeView().removeTreeElement(selectedView);
            tree.removeTreeElement(selectedView.getTreeElement());

            selection.newSelection(newSelectedView);
            GameBoardFacade.getInstance().setBoard(newSelectedView.getTreeElement().getBoard());
            GameBoardFacade.getInstance().getLegupUI().repaintBoard();
            GameBoardFacade.getInstance().getLegupUI().repaintTree();

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
