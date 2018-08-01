package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateBasicRuleCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    private Map<TreeElement, Rule> oldRules;
    private Map<TreeElement, Boolean> addNode;
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
        Tree tree = GameBoardFacade.getInstance().getTree();
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
                    TreeNode treeNode = tree.addNode(transition);
                    treeNode.getBoard().setModifiable(false);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeNode));
                    }
                    addNode.put(element, true);
                }
                else
                {
                    addNode.put(element, false);
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
                    TreeNode treeNode = tree.addNode(transition);
                    treeNode.getBoard().setModifiable(false);

                    if(transitionView.getChildView() == null)
                    {
                        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeNode));
                    }
                    addNode.put(element, true);
                }
                else
                {
                    addNode.put(element, false);
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
            return "There must be at least 1 tree puzzleElement selected to be able to be justified with a basic rule.";
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
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView selectedView : selectedViews)
        {
            TreeElement element = selectedView.getTreeElement();
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition)element;
                transition.setRule(oldRules.get(element));

                if(addNode.get(element))
                {
                    TreeNode childNode = transition.getChildNode();
                    tree.removeTreeElement(childNode);
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(childNode));
                }
            }
            else
            {
                TreeNodeView nodeView = (TreeNodeView)selectedView;
                TreeNode node = nodeView.getTreeElement();

                TreeTransition transition = node.getChildren().get(0);

                transition.setRule(oldRules.get(transition));

                if(addNode.get(element))
                {
                    TreeNode childNode = transition.getChildNode();
                    tree.removeTreeElement(childNode);
                    puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(childNode));
                }
            }
        }
        TreeElementView lastSelected = selectedViews.get(0);
        final Board newBoard = lastSelected.getTreeElement().getBoard();
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newBoard));
    }
}
