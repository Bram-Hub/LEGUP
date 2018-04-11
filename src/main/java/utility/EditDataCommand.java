package utility;

import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;
import ui.treeview.*;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class EditDataCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private ElementData oldData;
    private ElementData newData;

    private PuzzleElement elementView;
    private TreeElementView selectedView;
    private TreeElementView newSelectedView;
    private MouseEvent event;

    private TreeTransitionView transitionView;

    public EditDataCommand(PuzzleElement elementView, TreeElementView selectedView, MouseEvent event)
    {
        this.elementView = elementView;
        this.selectedView = selectedView;
        this.event = event;
        this.newData = elementView.getData();
        this.oldData = newData.copy();
        this.transition = null;
    }

    /**
     * Executes a command
     */
    @Override
    public void execute()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();

        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            if(transition == null)
            {
                transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
            }

            treeNode.getChildren().add(transition);
            transitionView = treeView.addNewTransitionView(nodeView, transition);

            selection.newSelection(transitionView);

            getInstance().getLegupUI().repaintTree();
            board = transition.getBoard();
            getInstance().getPuzzleModule().setCurrentBoard(board);
            newData = board.getElementData(index);
            oldData = newData.copy();
        }
        else
        {
            transitionView = (TreeTransitionView) selectedView;
            transition = transitionView.getTreeElement();
        }
        newSelectedView = transitionView;

        boardView.getElementController().changeCell(event, newData);

        if(newData.equals(oldData))
        {
            board.removeModifiedData(newData);
        }
        else
        {
            board.addModifiedData(newData);
        }
        transition.propagateChanges(newData);

        getInstance().getLegupUI().repaintBoard();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();
        if(!board.isModifiable())
        {
            return false;
        }
        else if(!board.getElementData(index).isModifiable())
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
        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();
        if(!board.isModifiable())
        {
            return "Board is not modifiable";
        }
        else if(!board.getElementData(index).isModifiable())
        {
            return "Data is not modifiable";
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();

        Board board = transition.getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            tree.removeTreeElement(transition);
            treeView.removeTreeElement(newSelectedView);

            selection.newSelection(selectedView);

            getInstance().getLegupUI().repaintTree();
            getInstance().getPuzzleModule().setCurrentBoard(treeNode.getBoard());
        }

        Board prevBoard = transition.getParentNode().getBoard();

        newData.setValueInt(oldData.getValueInt());
        board.notifyChange(newData);

        //System.err.println(newData.getValueInt() + " : " + oldData.getValueInt());

        if(prevBoard.getElementData(index).equals(newData))
        {
            board.removeModifiedData(newData);
        }
        else
        {
            board.addModifiedData(newData);
        }
        transition.propagateChanges(newData);
        getInstance().getLegupUI().repaintBoard();
    }
}
