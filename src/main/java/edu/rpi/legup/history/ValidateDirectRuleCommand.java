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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ValidateDirectRuleCommand class represents a command for validating and applying a DirectRule
 * to a set of selected tree elements. It extends the PuzzleCommand class and implements the
 * ICommand interface.
 */
public class ValidateDirectRuleCommand extends PuzzleCommand {
    private static final Logger LOGGER = LogManager.getLogger(History.class.getName());
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

    /** Executes the command to validate and apply the DirectRule. */
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

            TreeElementView childView = treeView.getElementView(childNode);
            if (childView == null) {
                LOGGER.error("Child view is null for child node: " + childNode);
                continue;
            }
            newSelection.addToSelection(childView);
        }

        TreeElementView firstSelectedView = selection.getFirstSelection();

        final TreeElement finalTreeElement;
        if (firstSelectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            if (nodeView.getChildrenViews().isEmpty()) {
                LOGGER.error("NodeView has no children views");
                return;
            }
            finalTreeElement = nodeView.getChildrenViews().get(0).getTreeElement();
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            TreeNodeView childView = transitionView.getChildView();
            if (childView == null) {
                LOGGER.error("Child view is null for transition view: " + transitionView);
                TreeNode childNode = transitionView.getTreeElement().getChildNode();
                childView = (TreeNodeView) treeView.getElementView(childNode);
                transitionView.setChildView(childView);
            }
            TreeTransition transition = transitionView.getTreeElement();
            if (transition.getParents().get(0).getChildren().isEmpty()) {
                transition.getParents().get(0).addChild(transition);
            }
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

    /** Undoes the validation command, restoring the previous state. */
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
