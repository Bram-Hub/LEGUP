package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.rules.Rule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateDirectRuleCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    private Map<TreeElement, Rule> oldRules;
    private Map<TreeTransition, TreeNode> addNode;
    private DirectRule newRule;

    /**
     * ValidateDesireRuleCommand Constructor creates a command for verifying a basic rule
     *
     * @param selection selection of tree elements
     * @param rule basic rule
     */
    public ValidateDirectRuleCommand(TreeViewSelection selection, DirectRule rule) {
        this.selection = selection.copy();
        this.newRule = rule;
        this.oldRules = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /** Executes an command */
    @Override
    public void executeCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for (TreeElementView selectedView : selectedViews) {
            TreeElement element = selectedView.getTreeElement();
            TreeTransitionView transitionView;
            if (element.getType() == TreeElementType.NODE) {
                TreeNodeView nodeView = (TreeNodeView) selectedView;
                transitionView = nodeView.getChildrenViews().get(0);
            } else {
                transitionView = (TreeTransitionView) selectedView;
            }
            TreeTransition transition = transitionView.getTreeElement();

            oldRules.put(transition, transition.getRule());
            transition.setRule(newRule);

            TreeNode childNode = transition.getChildNode();
            if (childNode == null) {
                childNode = addNode.get(transition);
                if (childNode == null) {
                    childNode = (TreeNode) tree.addTreeElement(transition);
                    addNode.put(transition, childNode);
                } else {
                    tree.addTreeElement(transition, childNode);
                }

                final TreeNode finalNode = childNode;
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalNode));
            }
            newSelection.addToSelection(treeView.getElementView(childNode));
        }
        TreeElementView firstSelectedView = selection.getFirstSelection();
        final TreeElement finalTreeElement;
        if (firstSelectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            finalTreeElement = nodeView.getChildrenViews().get(0).getTreeElement();
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            finalTreeElement = transitionView.getChildView().getTreeElement();
        }
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        List<TreeElementView> selectedViews = selection.getSelectedViews();
        if (selectedViews.isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
        }

        for (TreeElementView view : selectedViews) {
            if (view.getType() == TreeElementType.NODE) {
                TreeNodeView nodeView = (TreeNodeView) view;
                if (nodeView.getChildrenViews().size() != 1) {
                    return CommandError.ONE_CHILD.toString();
                }
            } else {
                TreeTransitionView transView = (TreeTransitionView) view;
                if (transView.getParentViews().size() > 1) {
                    return CommandError.CONTAINS_MERGE.toString();
                }
            }
        }
        return null;
    }

    /** Undoes an command */
    @Override
    public void undoCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        for (TreeElementView selectedView : selection.getSelectedViews()) {
            TreeElement element = selectedView.getTreeElement();
            TreeTransitionView transitionView;
            if (element.getType() == TreeElementType.NODE) {
                TreeNodeView nodeView = (TreeNodeView) selectedView;
                transitionView = nodeView.getChildrenViews().get(0);
            } else {
                transitionView = (TreeTransitionView) selectedView;
            }
            TreeTransition transition = transitionView.getTreeElement();
            transition.setRule(oldRules.get(transition));

            if (addNode.get(transition) != null) {
                final TreeNode childNode = transition.getChildNode();
                tree.removeTreeElement(childNode);
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(childNode));
            }
        }

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
