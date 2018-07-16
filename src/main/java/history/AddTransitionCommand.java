package history;

import app.GameBoardFacade;
import model.tree.Tree;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.treeview.TreeElementView;
import ui.treeview.TreeNodeView;
import ui.treeview.TreeTransitionView;
import ui.treeview.TreeView;

import static app.GameBoardFacade.getInstance;

public class AddTransitionCommand extends PuzzleCommand
{

    private TreeElementView selectedView;
    private TreeTransition transition;
    private TreeTransitionView transitionView;

    public AddTransitionCommand(TreeElementView selectedView)
    {
        this.selectedView = selectedView;
        this.transition = null;
        this.transitionView = null;
    }

    /**
     * Executes an command
     */
    @Override
    public void execute()
    {
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();

        TreeNodeView nodeView = (TreeNodeView) selectedView;
        TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

        if(transition == null)
        {
            transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
        }

        treeNode.getChildren().add(transition);
        transitionView = treeView.addTransitionView(nodeView, transition);

        getInstance().getLegupUI().repaintTree();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        if(selectedView != null && selectedView.getType() == TreeElementType.NODE)
        {
            return true;
        }
        else
        {
            return false;
        }
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
        if(selectedView == null)
        {
            return "No selected view";
        }
        else if(selectedView.getType() != TreeElementType.NODE)
        {
            return "Selected view is not a node";
        }
        else
        {
            return null;
        }
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();

        TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

        tree.removeTreeElement(transition);
        treeView.removeTreeElement(transitionView);

        getInstance().getLegupUI().repaintTree();
        getInstance().getPuzzleModule().setCurrentBoard(treeNode.getBoard());
    }
}
