package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidateContradictionRuleCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    private HashMap<TreeNode, ArrayList<TreeTransition>> saveElements;
    private Rule newRule;

    /**
     * ValidateContradictionRuleCommand Constructor - creates a edu.rpi.legup.puzzle command for validating a contradiction rule
     *
     * @param selection currently selected tree puzzleElement views
     * @param rule contradiction rule to set to all of the tree elements
     */
    public ValidateContradictionRuleCommand(TreeViewSelection selection, Rule rule)
    {
        this.selection = selection;
        this.newRule = rule;
        this.saveElements = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void executeCommand()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeNode node;
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition) element;
                node = transition.getParents().get(0);
            }
            else
            {
                node = (TreeNode) element;
            }

            if(!node.getChildren().isEmpty())
            {
                ArrayList<TreeTransition> save = new ArrayList<>(node.getChildren());
                saveElements.put(node, save);
            }

            node.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(n)));

            node.getChildren().clear();

            TreeTransition transition = tree.addNewTransition(node);
            TreeNode treeNode = new TreeNode(transition.getBoard().copy());
            transition.setChildNode(treeNode);
            treeNode.setParent(transition);

            transition.setRule(newRule);

            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(treeNode));

            TreeTransitionView transitionView = (TreeTransitionView) treeView.getElementView(transition);
            newSelection.addToSelection(transitionView);
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getErrorString()
    {
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand()
    {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for(TreeElementView view : selectedViews)
        {
            TreeElement element = view.getTreeElement();
            TreeNode node;
            if(element.getType() == TreeElementType.TRANSITION)
            {
                TreeTransition transition = (TreeTransition) element;
                node = transition.getParents().get(0);
            }
            else
            {
                node = (TreeNode) element;
            }
            node.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(n)));
            node.getChildren().clear();

            ArrayList<TreeTransition> save = saveElements.get(element);

            if(save != null && !save.isEmpty())
            {
                node.getChildren().addAll(save);
                node.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(n)));
            }
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
