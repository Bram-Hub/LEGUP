package utility;

import app.GameBoardFacade;
import model.tree.*;
import ui.treeview.*;

import java.util.ArrayList;

public class DeleteTreeElementCommand extends PuzzleCommand
{
    private ArrayList<TreeElementView> selectedViews;

    public DeleteTreeElementCommand(ArrayList<TreeElementView> selectedViews)
    {
        this.selectedViews = (ArrayList<TreeElementView>)selectedViews.clone();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
        TreeView treeView = treePanel.getTreeView();
        TreeSelection selection = treeView.getTreeSelection();


        TreeElementView firstSelectedView = selectedViews.get(0);
        TreeElementView newSelectedView;
        if(firstSelectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            newSelectedView = nodeView.getParentViews().get(0);
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            newSelectedView = transitionView.getParentView();
        }
        selection.newSelection(newSelectedView);

        for(int i = 0; i < selectedViews.size(); i++)
        {
            TreeElementView selectedView = selectedViews.get(i);
            tree.removeTreeElement(selectedView.getTreeElement());
            treeView.removeTreeElement(selectedView);
        }

        GameBoardFacade.getInstance().setBoard(newSelectedView.getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        if(selectedViews.isEmpty())
        {
            return false;
        }

        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE && ((TreeNode)element).isRoot())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getExecutionError()
    {
        if(selectedViews.isEmpty())
        {
            return "Selection is Empty";
        }

        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE && ((TreeNode)element).isRoot())
            {
                return "Selection contains the root node";
            }
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
        TreeView treeView = treePanel.getTreeView();
        TreeSelection selection = treeView.getTreeSelection();

        for(int i = 0; i < selectedViews.size(); i++)
        {
            TreeElementView selectedView = selectedViews.get(i);
            if(selectedView.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView) selectedView;
                TreeNode node = nodeView.getTreeElement();
                for(TreeTransitionView view : nodeView.getParentViews())
                {
                    view.setChildView(nodeView);
                }
                for(TreeTransition tran : nodeView.getTreeElement().getParents())
                {
                    tran.setChildNode(node);
                }
            }
            else
            {
                TreeTransitionView transitionView = (TreeTransitionView) selectedView;
                TreeTransition treeTransition = transitionView.getTreeElement();
                transitionView.getParentView().addChildrenView(transitionView);
                treeTransition.getParentNode().addChild(treeTransition);
            }
        }

        TreeElementView firstSelectedView = selectedViews.get(0);
        selection.getSelection().addAll(selectedViews);

        GameBoardFacade.getInstance().setBoard(firstSelectedView.getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }
}
