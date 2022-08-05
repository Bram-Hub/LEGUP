package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyDefaultBasicRuleCommand extends PuzzleCommand {

    private TreeViewSelection selection;
    private BasicRule rule;
    private Map<TreeNode, TreeTransition> addMap;

    /**
     * ApplyDefaultBasicRuleCommand Constructor creates a command for applying the default of a basic rule
     *
     * @param selection selection of tree element views
     * @param rule      basic rule for the command
     */
    public ApplyDefaultBasicRuleCommand(TreeViewSelection selection, BasicRule rule) {
        this.selection = selection.copy();
        this.rule = rule;
        this.addMap = new HashMap<>();
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
            return CommandError.DEFAULT_APPLICATION + " - " + CommandError.NO_SELECTED_VIEWS.toString();
        }
        else {
            for (TreeElementView view : selectedViews) {
                TreeElement element = view.getTreeElement();
                if (element.getType() == TreeElementType.NODE) {
                    TreeNode node = (TreeNode) element;
                    if (!node.getChildren().isEmpty()) {
                        return CommandError.DEFAULT_APPLICATION + " - " + CommandError.NO_CHILDREN.toString();
                    }
                    else {
                        if (rule.getDefaultBoard(node) == null) {
                            return CommandError.DEFAULT_APPLICATION + " - " + "This selection contains a tree element that this rule cannot be applied to.";
                        }
                    }
                }
                else {
                    return CommandError.DEFAULT_APPLICATION + " - " + CommandError.SELECTION_CONTAINS_TRANSITION.toString();
                }
            }
        }
        return null;
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

        for (TreeElementView selectedView : selection.getSelectedViews()) {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode node = nodeView.getTreeElement();
            TreeTransition transition = addMap.get(node);
            TreeNode childNode;
            if (transition == null) {
                transition = (TreeTransition) tree.addTreeElement(node);
                childNode = (TreeNode) tree.addTreeElement(transition);
                addMap.put(node, transition);
            }
            else {
                tree.addTreeElement(node, transition);
                childNode = transition.getChildNode();
            }

            transition.setRule(rule);
            Board defaultBoard = rule.getDefaultBoard(node);
            transition.setBoard(defaultBoard);
            Board copyBoard = defaultBoard.copy();
            copyBoard.setModifiable(false);
            childNode.setBoard(copyBoard);

            final TreeTransition finalTran = transition;
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalTran));

            newSelection.addToSelection(treeView.getElementView(childNode));
        }

        final TreeElement finalTreeElement = newSelection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        for (TreeElementView selectedView : selection.getSelectedViews()) {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode node = nodeView.getTreeElement();
            final TreeTransition transition = addMap.get(node);

            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));
        }

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
