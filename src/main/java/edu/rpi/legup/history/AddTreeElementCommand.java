package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.TreeElementView;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTreeElementCommand extends PuzzleCommand
{

    private TreeViewSelection selection;

    private Map<TreeElement, TreeElement> addChild;

    public AddTreeElementCommand(TreeViewSelection selection)
    {
        this.selection = selection;
        this.addChild = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        if(!canExecute())
        {
            return;
        }

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeElement child = addChild.get(element);
            if(element.getType() == TreeElementType.NODE)
            {
                TreeNode node = (TreeNode)element;
                if(child == null)
                {
                    child = new TreeTransition(node, node.getBoard().copy());
                }
                node.addChild((TreeTransition) child);
            }
            else
            {
                TreeTransition transition = (TreeTransition)element;
                if(child == null)
                {
                    child = new TreeNode(transition.getBoard().copy());
                }
                transition.setChildNode((TreeNode)child);
                ((TreeNode) child).setParent(transition);
            }
            TreeElement finalChild = child;
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalChild));

            TreeElementView childView = treeView.getElementView(child);
            newSelection.addToSelection(childView);
            addChild.put(element, child);
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
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
        else
        {
            for(TreeElementView view : selectedViews)
            {
                TreeElement element = view.getTreeElement();
                if(element.getType() == TreeElementType.TRANSITION)
                {
                    TreeTransition transition = (TreeTransition)element;
                    if(transition.getChildNode() != null)
                    {
                        return false;
                    }
                }
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
            return "There must be a selected tree puzzleElement to add to it";
        }
        else
        {
            for(TreeElementView view : selectedViews)
            {
                TreeElement element = view.getTreeElement();
                if(element.getType() == TreeElementType.TRANSITION)
                {
                    TreeTransition transition = (TreeTransition)element;
                    if(transition.getChildNode() != null)
                    {
                        return "There is already a child node to a selected transition";
                    }
                }
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
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeElement child = addChild.get(element);
            tree.removeTreeElement(child);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(child));
            newSelection.addToSelection(treeView.getElementView(element));
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }
}