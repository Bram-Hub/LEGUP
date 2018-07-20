package history;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.rules.Rule;
import model.tree.*;
import ui.treeview.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateBasicRuleCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    private Map<TreeElement, Rule> oldRules;
    private Map<TreeElement, TreeNode> addNode;
    private Rule newRule;

    public ValidateBasicRuleCommand(TreeViewSelection selection, Rule rule)
    {
        this.selection = selection;
        this.newRule = rule;
        this.oldRules = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        TreeElementView lastSelected = null;

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                TreeTransitionView transitionView = (TreeTransitionView)selectedView;
                oldRules.put(transition, transition.getRule());
                transition.setRule(newRule);

                if(transition.getChildNode() == null)
                {
                    TreeNode treeNode;
                    if(addNode.get(element) == null)
                    {
                        Board copyBoard = transition.getBoard().copy();
                        copyBoard.setModifiable(false);
                        treeNode = new TreeNode(copyBoard);
                        addNode.put(element, treeNode);
                        treeNode.setParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeNode));
                    }
                }
                else
                {
                    addNode.put(element, null);
                }
                lastSelected = transitionView.getChildView();
            }
            else
            {
                TreeNodeView nodeView = (TreeNodeView)selectedView;

                TreeTransitionView transitionView = nodeView.getChildrenViews().get(0);
                TreeTransition transition = transitionView.getTreeElement();
                oldRules.put(transition, transition.getRule());
                transition.setRule(newRule);

                if(transition.getChildNode() == null)
                {
                    TreeNode treeNode;
                    if(addNode.get(element) == null)
                    {
                        Board copyBoard = transition.getBoard().copy();
                        copyBoard.setModifiable(false);
                        treeNode = new TreeNode(copyBoard);
                        addNode.put(element, treeNode);
                        treeNode.setParent(transition);
                    }
                    else
                    {
                        treeNode = addNode.get(element);
                    }
                    transition.setChildNode(treeNode);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeNode));
                    }
                }
                else
                {
                    addNode.put(element, null);
                }
                lastSelected = transitionView.getChildView();
            }
        }
        final TreeViewSelection newSelection = new TreeViewSelection(lastSelected);
        final Board newBoard = lastSelected.getTreeElement().getBoard();
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newBoard));
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
            if(view.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)view;
                TreeNode node = nodeView.getTreeElement();
                if(node.getChildren().size() != 1)
                {
                    return false;
                }
            }
            else
            {
                TreeTransitionView transView = (TreeTransitionView)view;
                if(transView.getParentViews().size() > 1)
                {
                    return false;
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
            return "There must be at least 1 tree element selected to be able to be justified with a basic rule.";
        }

        for(TreeElementView view : selectedViews)
        {
            if(view.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)view;
                if(nodeView.getChildrenViews().size() != 1)
                {
                    return "Nodes must have 1 child transition to be able to be justified with a basic rule.";
                }
            }
            else
            {
                TreeTransitionView transView = (TreeTransitionView)view;
                if(transView.getParentViews().size() > 1)
                {
                    return "Transitions must not be apart of a merge to be able to be justified with a basic rule.";
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
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                TreeTransitionView transitionView = (TreeTransitionView)selectedView;
                transition.setRule(oldRules.get(element));

                if(addNode.get(element) != null)
                {
                    tree.removeTreeElement(transition.getChildNode());
                    treeView.removeTreeElement(transitionView.getChildView());
                }
            }
            else
            {
                TreeNodeView nodeView = (TreeNodeView)selectedView;

                TreeTransitionView transitionView = nodeView.getChildrenViews().get(0);
                TreeTransition transition = transitionView.getTreeElement();

                transition.setRule(oldRules.get(element));

                if(addNode.get(element) != null)
                {
                    tree.removeTreeElement(transition.getChildNode());
                    treeView.removeTreeElement(transitionView.getChildView());
                }
            }
        }
        final TreeViewSelection newSelection = new TreeViewSelection(selectedViews);
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }
}
