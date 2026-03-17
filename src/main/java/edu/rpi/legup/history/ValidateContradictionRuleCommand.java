package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * The ValidateContradictionRuleCommand class represents a command for validating and applying a
 * ContradictionRule within a tree structure. It extends the PuzzleCommand class and implements the
 * ICommand interface.
 */
public class ValidateContradictionRuleCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    private Map<TreeNode, ArrayList<TreeTransition>> saveElements;
    private ContradictionRule newRule;
    private Map<TreeElement, TreeTransition> addTran;

    /**
     * ValidateContradictionRuleCommand Constructor creates a puzzle command for verifying a
     * contradiction rule
     *
     * @param selection currently selected tree puzzleElement views
     * @param rule contradiction rule to be set to all the tree elements
     */
    public ValidateContradictionRuleCommand(TreeViewSelection selection, ContradictionRule rule) {
        this.selection = selection.copy();
        this.newRule = rule;
        this.saveElements = new HashMap<>();
        this.addTran = new HashMap<>();
    }

    /** Executes the command to validate and apply the ContradictionRule. */
    @Override
    public void executeCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for (TreeElementView view : selectedViews) {
            TreeElement treeElement = view.getTreeElement();
            TreeNode treeNode;
            if (treeElement.getType() == TreeElementType.TRANSITION) {
                TreeTransition transition = (TreeTransition) treeElement;
                treeNode = transition.getParents().get(0);
            } else {
                treeNode = (TreeNode) treeElement;
            }

            if (!treeNode.getChildren().isEmpty()) {
                ArrayList<TreeTransition> save = new ArrayList<>(treeNode.getChildren());
                saveElements.put(treeNode, save);
            }

            treeNode.getChildren()
                    .forEach(
                            n ->
                                    puzzle.notifyTreeListeners(
                                            listener -> listener.onTreeElementRemoved(n)));

            treeNode.getChildren().clear();

            TreeTransition transition = addTran.get(treeElement);
            if (transition == null) {
                transition = tree.addNewTransition(treeNode);
                transition.setRule(newRule);
                transition.getBoard().setModifiable(false);
                tree.addTreeElement(transition);
            } else {
                transition.getBoard().setModifiable(false);
                tree.addTreeElement(treeNode, transition);
            }

            final TreeTransition finalTran = transition;
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalTran));

            newSelection.addToSelection(treeView.getElementView(transition));
        }

        TreeElementView firstSelectedView = selection.getFirstSelection();
        final TreeElement finalTreeElement;
        if (firstSelectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            if (!nodeView.getChildrenViews().isEmpty()) {
                finalTreeElement = nodeView.getChildrenViews().get(0).getTreeElement();
            } else {
                finalTreeElement = null;
            }
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            if (transitionView.getChildView() != null) {
                finalTreeElement = transitionView.getChildView().getTreeElement();
            } else {
                finalTreeElement = null;
            }
        }

        if (finalTreeElement != null) {
            puzzle.notifyBoardListeners(
                    listener -> listener.onTreeElementChanged(finalTreeElement));
        }
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

        // Add confirmation if multiple nodes that have changes will be removed
        // A contradiction is typically applied to a given state, which is represented
        // by a TreeNode. All further transitions and nodes do not matter if there
        // is a contradiction at this node, and so this function disconnects the tree,
        // potentially causing the user to lose progress.
        boolean removingChildren = false;
        for (TreeElementView view : selectedViews) {
            TreeElement treeElement = view.getTreeElement();
            TreeNode treeNode;
            if (treeElement instanceof TreeNode) {
                treeNode = (TreeNode) treeElement;
            } else {
                TreeTransition treeTransition = (TreeTransition) treeElement;
                treeNode = treeTransition.getParents().getFirst();
            }
            // If any selected view has children, prompt for confirmation
            if (!treeNode.getChildren().isEmpty()) {
                removingChildren = true;
                break;
            }
        }

        if (removingChildren) {
            int confirmReset =
                    JOptionPane.showConfirmDialog(
                            null,
                            "Applying a Contradiction Rule here may cause a large section of the tree to be removed. Continue?",
                            "Confirm Contradiction Rule",
                            JOptionPane.YES_NO_OPTION);
            if (confirmReset == JOptionPane.NO_OPTION) {
                return CommandError.EMPTY.toString();
            }
        }

        for (TreeElementView view : selectedViews) {
            if (view.getType() == TreeElementType.TRANSITION) {
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
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for (TreeElementView view : selectedViews) {
            TreeElement element = view.getTreeElement();
            TreeNode node;
            if (element.getType() == TreeElementType.TRANSITION) {
                TreeTransition transition = (TreeTransition) element;
                node = transition.getParents().get(0);
            } else {
                node = (TreeNode) element;
            }
            node.getChildren()
                    .forEach(
                            n ->
                                    puzzle.notifyTreeListeners(
                                            listener -> listener.onTreeElementRemoved(n)));
            node.getChildren().clear();

            ArrayList<TreeTransition> save = saveElements.get(node);

            if (save != null) {
                node.getChildren().addAll(save);
                node.getChildren()
                        .forEach(
                                n ->
                                        puzzle.notifyTreeListeners(
                                                listener -> listener.onTreeElementAdded(n)));
            }
        }

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
