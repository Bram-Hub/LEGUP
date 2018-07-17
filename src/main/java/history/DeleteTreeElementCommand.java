package history;

import app.GameBoardFacade;
import model.tree.*;
import ui.treeview.*;

import java.util.List;

public class DeleteTreeElementCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    /**
     * DeleteTreeElementCommand Constructor - creates a PuzzleCommand for deleting a tree element
     *
     * @param selection the currently selected tree elements before the command is executed
     */
    public DeleteTreeElementCommand(TreeViewSelection selection)
    {
        this.selection = selection;
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

        List<TreeElementView> selectedViews = selection.getSelectedViews();

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
        List<TreeElementView> selectedViews = selection.getSelectedViews();
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
        List<TreeElementView> selectedViews = selection.getSelectedViews();
        if(selectedViews.isEmpty())
        {
            return "There must be a selected tree element to delete it";
        }

        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            if(element.getType() == TreeElementType.NODE && ((TreeNode)element).isRoot())
            {
                return "The selection must not contain the root node";
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
        List<TreeElementView> selectedViews = selection.getSelectedViews();

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
        selection.getSelectedViews().addAll(selectedViews);

        GameBoardFacade.getInstance().setBoard(firstSelectedView.getTreeElement().getBoard());
        GameBoardFacade.getInstance().getLegupUI().repaintBoard();
        GameBoardFacade.getInstance().getLegupUI().repaintTree();
    }
}
