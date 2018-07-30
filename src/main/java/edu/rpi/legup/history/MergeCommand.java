package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.rules.MergeRule;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.treeview.*;

import java.util.ArrayList;
import java.util.List;

public class MergeCommand extends PuzzleCommand
{
    private TreeViewSelection selection;

    public MergeCommand(TreeViewSelection selection)
    {
        this.selection = selection;
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
        List<TreeElementView> selectedViews = selection.getSelectedViews();

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

        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transMerge));
        puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(mergedNode));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
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
        if(selection.getSelectedViews().isEmpty())
        {
            return false;
        }
        else if(selection.getSelectedViews().size() == 1)
        {

        }


        for(TreeElementView view : selection.getSelectedViews())
        {
            if(view.getType() == TreeElementType.NODE)
            {
                TreeNodeView nodeView = (TreeNodeView)view;

            }
            else
            {
                TreeTransitionView transView = (TreeTransitionView)view;

            }
        }
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
