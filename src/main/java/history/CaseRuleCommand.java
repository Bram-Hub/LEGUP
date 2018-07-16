package history;

import model.Puzzle;
import model.gameboard.Board;
import model.observer.ITreeListener;
import model.rules.CaseRule;
import model.tree.Tree;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.boardview.PuzzleElement;
import ui.treeview.TreeNodeView;
import ui.treeview.TreeViewSelection;

import java.util.List;

import static app.GameBoardFacade.getInstance;

public class CaseRuleCommand extends PuzzleCommand
{
    private PuzzleElement elementView;
    private TreeViewSelection selection;

    /**
     * CaseRuleCommand Constructor - creates a command for validating a case rule
     *
     * @param elementView currently selected puzzle element view that is being edited
     * @param selection currently selected tree element views that is being edited
     */
    public CaseRuleCommand(PuzzleElement elementView, TreeViewSelection selection)
    {
        this.elementView = elementView;
        this.selection = selection.copy();
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        TreeNodeView nodeView = (TreeNodeView)selection.getFirstSelection();
        TreeNode node = nodeView.getTreeElement();
        Board board = node.getBoard();
        CaseRule caseRule = board.getCaseRule();
        List<Board> cases = caseRule.getCases(board, elementView.getIndex());

        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = getInstance().getTree();
        for(Board b: cases)
        {
            TreeTransition transition = tree.addNewTransition(node);
            b.setModifiable(false);
            transition.setBoard(b);
            transition.setRule(caseRule);

            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(transition));

            TreeNode n = tree.addNode(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(n));
        }
        selection.newSelection(nodeView.getChildrenViews().get(0).getChildView());
        getInstance().getPuzzleModule().setCurrentBoard(node.getChildren().get(0).getBoard());
        getInstance().getLegupUI().repaintTree();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        if(selection.getSelection().size() > 1)
        {
            return false;
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
        if(selection.getSelection().size() > 1)
        {
            return "Tree Selection must exactly 1 Tree Node";
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {

    }
}
