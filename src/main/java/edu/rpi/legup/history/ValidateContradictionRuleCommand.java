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

public class ValidateContradictionRuleCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    private Map<TreeNode, ArrayList<TreeTransition>> saveElements;
    private ContradictionRule newRule;
    private Map<TreeElement, TreeTransition> addTran;

    /**
     * ValidateContradictionRuleCommand Constructor creates a puzzle command for verifying a contradiction rule
     *
     * @param selection currently selected tree puzzleElement views
     * @param rule      contradiction rule to set to all of the tree elements
     */
    public ValidateContradictionRuleCommand(TreeViewSelection selection, ContradictionRule rule) {
        this.selection = selection.copy();
        this.newRule = rule;
        this.saveElements = new HashMap<>();
        this.addTran = new HashMap<>();
    }

    /**
     * Executes an command
     */
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
            }
            else {
                treeNode = (TreeNode) treeElement;
            }

            if (!treeNode.getChildren().isEmpty()) {
                ArrayList<TreeTransition> save = new ArrayList<>(treeNode.getChildren());
                saveElements.put(treeNode, save);
            }

            treeNode.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(n)));

            treeNode.getChildren().clear();

            TreeTransition transition = addTran.get(treeElement);
            if (transition == null) {
                transition = tree.addNewTransition(treeNode);
                transition.setRule(newRule);
                tree.addTreeElement(transition);
            }
            else {
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
            finalTreeElement = nodeView.getChildrenViews().get(0).getTreeElement();
        }
        else {
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
     * otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        List<TreeElementView> selectedViews = selection.getSelectedViews();
        if (selectedViews.isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
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

    /**
     * Undoes an command
     */
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
            }
            else {
                node = (TreeNode) element;
            }
            node.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(n)));
            node.getChildren().clear();

            ArrayList<TreeTransition> save = saveElements.get(node);

            if (save != null) {
                node.getChildren().addAll(save);
                node.getChildren().forEach(n -> puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(n)));
            }
        }

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
