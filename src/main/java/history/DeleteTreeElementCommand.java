package history;

import app.GameBoardFacade;
import model.tree.*;
import ui.treeview.*;

import java.util.ArrayList;

public class DeleteTreeElementCommand extends PuzzleCommand
{
    private ArrayList<TreeElementView> selectedViews;

    /**
     * DeleteTreeElementCommand Constructor - creates a PuzzleCommand for deleting a tree element
     *
     * @param selectedViews the currently selected tree elements before the command is executed
     */
    @SuppressWarnings("unchecked")
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
            newSelectedView = nodeView.getParentView();
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            newSelectedView = transitionView.getParentViews().get(0);
        }
        selection.newSelection(newSelectedView);

        for(TreeElementView selectedView : selectedViews)
        {
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

        for(TreeElementView selectedView : selectedViews)
        {
            if(selectedView.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView) selectedView;
                TreeNode node = nodeView.getTreeElement();

                nodeView.getParentView().setChildView(nodeView);
                node.getParent().setChildNode(node);
            }
            else
            {
                TreeTransitionView transitionView = (TreeTransitionView) selectedView;
                TreeTransition treeTransition = transitionView.getTreeElement();
                transitionView.getParentViews().forEach((TreeNodeView view) -> view.addChildrenView(transitionView));
                treeTransition.getParents().forEach((TreeNode node) -> node.addChild(treeTransition));
            }
        }

        TreeElementView firstSelectedView = selectedViews.get(0);
        selection.getSelection().addAll(selectedViews);

        GameBoardFacade.getInstance().setBoard(firstSelectedView.getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }
}
