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

public class ValidateBasicRuleCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    private Map<TreeElement, Rule> oldRules;
    private Map<TreeTransition, TreeNode> addNode;
    private Rule newRule;

    public ValidateBasicRuleCommand(TreeViewSelection selection, Rule rule) {
        this.selection = selection.copy();
        this.newRule = rule;
        this.oldRules = new HashMap<>();
        this.addNode = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void executeCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
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
        final Board finalBoard;
        if (firstSelectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            finalBoard = nodeView.getChildrenViews().get(0).getTreeElement().getBoard();
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            finalBoard = transitionView.getChildView().getTreeElement().getBoard();
        }
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(finalBoard));
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

    /**
     * Undoes an command
     */
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

        final Board newBoard = selection.getFirstSelection().getTreeElement().getBoard();
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(newBoard));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
