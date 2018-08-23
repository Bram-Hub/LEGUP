package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.MergeRule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.*;

import java.util.ArrayList;
import java.util.List;

public class MergeCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    public MergeCommand(TreeViewSelection selection) {
        this.selection = selection;
    }

    /**
     * Executes an command
     */
    @Override
    public void executeCommand() {
        List<TreeElementView> selectedViews = selection.getSelectedViews();

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        ArrayList<TreeNode> mergingNodes = new ArrayList<>();
        ArrayList<Board> mergingBoards = new ArrayList<>();
        for (TreeElementView view : selectedViews) {
            TreeNode node = ((TreeNodeView) view).getTreeElement();
            mergingNodes.add(node);
            mergingBoards.add(node.getBoard());
        }

        TreeNode lca = tree.getLowestCommonAncestor(mergingNodes);
        Board lcaBoard = lca.getBoard();

        Board mergedBoard = lcaBoard.mergedBoard(lcaBoard, mergingBoards);

        TreeNode mergedNode = new TreeNode(mergedBoard.copy());
        TreeTransition transMerge = new TreeTransition(mergedBoard);

        transMerge.setRule(new MergeRule());
        transMerge.setChildNode(mergedNode);
        mergedNode.setParent(transMerge);

        for (TreeElementView elementView : selectedViews) {
            TreeNodeView nodeView = (TreeNodeView) elementView;
            TreeNode node = nodeView.getTreeElement();

            node.addChild(transMerge);

            transMerge.addParent(node);
        }

        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transMerge));

        final TreeViewSelection newSelection = new TreeViewSelection();
        newSelection.addToSelection(treeView.getElementView(mergedNode));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        TreeTransition transition = ((TreeNode)selection.getFirstSelection().getTreeElement()).getChildren().get(0);
        tree.removeTreeElement(transition);

        puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
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
        if(selectedViews.size() < 2) {
            return "There must be at least 2 tree nodes to merge.";
        }

        List<TreeNode> nodeList = new ArrayList<>();
        for (TreeElementView view : selection.getSelectedViews()) {
            if (view.getType() == TreeElementType.NODE) {
                TreeNodeView nodeView = (TreeNodeView) view;
                if(!nodeView.getChildrenViews().isEmpty()) {
                    return "All selected tree nodes must have no children.";
                }
                nodeList.add(nodeView.getTreeElement());
            } else {
                return "All selected tree elements must be nodes.";
            }
        }
        return null;
    }
}
