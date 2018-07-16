package history;

import app.GameBoardFacade;
import model.Puzzle;
import model.gameboard.Board;
import model.observer.ITreeListener;
import model.rules.MergeRule;
import model.tree.Tree;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.treeview.TreeElementView;
import ui.treeview.TreeNodeView;
import ui.treeview.TreeViewSelection;
import ui.treeview.TreeView;

import java.util.ArrayList;
import java.util.List;

public class MergeCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    public MergeCommand(TreeViewSelection selection)
    {
        this.selection = selection.copy();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        if(!canExecute())
        {
            return;
        }

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        List<TreeElementView> selectedViews = selection.getSelection();

        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        ArrayList<TreeNode> mergingNodes = new ArrayList<>();
        ArrayList<Board> mergingBoards = new ArrayList<>();
        for(TreeElementView view : selectedViews)
        {
            TreeNode node = ((TreeNodeView)view).getTreeElement();
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

        for(TreeElementView elementView : selectedViews)
        {
            TreeNodeView nodeView = (TreeNodeView)elementView;
            TreeNode node = nodeView.getTreeElement();

            node.addChild(transMerge);

            transMerge.addParent(node);
        }

        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(transMerge));
        puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(mergedNode));
        puzzle.notifyTreeListeners(l -> l.onTreeSelectionChanged(selection));
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {

    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
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
        return null;
    }
}
