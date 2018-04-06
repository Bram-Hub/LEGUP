package utility;

import model.Puzzle;
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
    }

    public EditDataCommand(TreeTransition transition, ElementData oldData, ElementData newData)
    {
        this.transition = transition;
        this.oldData = oldData.copy();
        this.newData = newData.copy();
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

            TreeTransition transition = tree.addNewTransition(treeNode);
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
        }
        newSelectedView = transitionView;

        boardView.getElementController().changeCell(event, newData);

        if(newData.equals(oldData))
        {
            newData.setModified(false);
            board.removeModifiedData(newData);
        }
        else
        {
            newData.setModified(true);
            board.addModifiedData(newData);
        }
        transitionView.getTreeElement().propagateChanges(newData);

        getInstance().getLegupUI().repaintBoard();
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

        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            tree.removeTreeElement(newSelectedView.getTreeElement());
            treeView.removeTreeElement(newSelectedView);

            selection.newSelection(selectedView);

            getInstance().getLegupUI().repaintTree();
            board = treeNode.getBoard();
            getInstance().getPuzzleModule().setCurrentBoard(board);
            oldData = board.getElementData(index);
        }
        else
        {
            transitionView = (TreeTransitionView) selectedView;

            Board prevBoard = transitionView.getParentView().getTreeElement().getBoard();

            board.notifyChange(oldData);
            oldData = board.getElementData(index);

            if(prevBoard.getElementData(index).equals(oldData))
            {
                oldData.setModified(false);
                board.removeModifiedData(oldData);
            }
            else
            {
                oldData.setModified(true);
                board.addModifiedData(oldData);
            }
            transitionView.getTreeElement().propagateChanges(oldData);
        }
        getInstance().getLegupUI().repaintBoard();
    }
}
