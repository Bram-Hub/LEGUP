package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.*;

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
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

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

        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            tree.removeTreeElement(element);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(element));
        }

        final TreeViewSelection newSelection = new TreeViewSelection(newSelectedView);
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newSelectedView.getTreeElement().getBoard()));
        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeSelectionChanged(newSelection));
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
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        List<TreeElementView> selectedViews = selection.getSelectedViews();

        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                node.getParent().setChildNode(node);

                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(node));
            }
            else
            {
                TreeTransition treeTransition = (TreeTransition)element;
                treeTransition.getParents().forEach(node -> node.addChild(treeTransition));

                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeTransition));
            }
        }

        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(selection.getFirstSelection().getTreeElement().getBoard()));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
