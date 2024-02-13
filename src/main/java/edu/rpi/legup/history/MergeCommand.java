package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.MergeRule;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MergeCommand extends PuzzleCommand {
  private TreeViewSelection selection;
  private TreeTransition transition;

  /**
   * Merge Command Constructor create a command for merging tree nodes.
   *
   * @param selection selection of tree elements
   */
  public MergeCommand(TreeViewSelection selection) {
    this.selection = selection.copy();
    this.transition = null;
  }

  /** Executes an command */
  @Override
  public void executeCommand() {
    List<TreeElementView> selectedViews = selection.getSelectedViews();

    TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
    Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

    TreeNode mergedNode;
    if (transition == null) {
      List<TreeNode> mergingNodes = new ArrayList<>();
      List<Board> mergingBoards = new ArrayList<>();
      for (TreeElementView view : selectedViews) {
        TreeNode node = ((TreeNodeView) view).getTreeElement();
        mergingNodes.add(node);
        mergingBoards.add(node.getBoard());
      }

      TreeNode lca = Tree.getLowestCommonAncestor(mergingNodes);
      Board lcaBoard = lca.getBoard();

      Board mergedBoard = lcaBoard.mergedBoard(lcaBoard, mergingBoards);

      mergedNode = new TreeNode(mergedBoard.copy());
      transition = new TreeTransition(mergedBoard);
      transition.setRule(new MergeRule());
      transition.setChildNode(mergedNode);
      mergedNode.setParent(transition);
    } else {
      mergedNode = transition.getChildNode();
    }

    transition.getParents().clear();
    for (TreeElementView elementView : selectedViews) {
      TreeNode node = (TreeNode) elementView.getTreeElement();

      node.addChild(transition);
      transition.addParent(node);
    }

    puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));

    final TreeViewSelection newSelection = new TreeViewSelection();
    newSelection.addToSelection(treeView.getElementView(mergedNode));
    puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
  }

  /** Undoes an command */
  @Override
  public void undoCommand() {
    Tree tree = GameBoardFacade.getInstance().getTree();
    Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

    TreeTransition transition =
        ((TreeNode) selection.getFirstSelection().getTreeElement()).getChildren().get(0);
    tree.removeTreeElement(transition);

    puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));
    puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
  }

  /**
   * Gets the reason why the command cannot be executed
   *
   * @return if command cannot be executed, returns reason for why the command cannot be executed,
   *     otherwise null if command can be executed
   */
  @Override
  public String getErrorString() {
    Tree tree = GameBoardFacade.getInstance().getTree();
    List<TreeElementView> selectedViews = selection.getSelectedViews();
    if (selectedViews.size() < 2) {
      return CommandError.TWO_TO_MERGE.toString();
    }

    List<TreeNode> nodeList = new ArrayList<>();
    for (TreeElementView view : selection.getSelectedViews()) {
      if (view.getType() == TreeElementType.NODE) {
        TreeNodeView nodeView = (TreeNodeView) view;
        if (!nodeView.getChildrenViews().isEmpty()) {
          return CommandError.NO_CHILDREN.toString();
        }
        nodeList.add(nodeView.getTreeElement());
      } else {
        return CommandError.SELECTION_CONTAINS_TRANSITION.toString();
      }
    }

    List<TreeNode> mergingNodes = new ArrayList<>();
    for (TreeElementView view : selectedViews) {
      TreeNode node = ((TreeNodeView) view).getTreeElement();
      mergingNodes.add(node);
    }

    TreeNode lca = Tree.getLowestCommonAncestor(mergingNodes);
    if (lca == null) {
      return "Unable to merge tree elements.";
    }
    Set<TreeElement> leafNodes = tree.getLeafTreeElements(lca);
    if (leafNodes.size() != mergingNodes.size()) {
      //            return "Unable to merge tree elements.";
    }

    for (TreeNode node : mergingNodes) {
      if (!leafNodes.contains(node)) {
        //                return "Unable to merge tree elements.";
      }
    }

    return null;
  }
}
