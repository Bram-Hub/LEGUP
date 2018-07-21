package history;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.observer.ITreeListener;
import model.tree.*;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;
import ui.treeview.*;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class EditDataCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private boolean hasAddedNode;
    private ElementData saveData;
    private ElementData data;

    private PuzzleElement elementView;
    private TreeViewSelection selection;
    private TreeElementView newSelectedView;
    private MouseEvent event;

    /**
     * EditDataCommand Constructor - create a puzzle command for editing a board
     *
     * @param elementView currently selected puzzle element view that is being edited
     * @param selection currently selected tree element views that is being edited
     * @param event mouse event
     */
    public EditDataCommand(PuzzleElement elementView, TreeViewSelection selection, MouseEvent event)
    {
        this.elementView = elementView;
        this.selection = selection;
        this.event = event;
        this.data = null;
        this.saveData = null;
        this.transition = null;
        this.hasAddedNode = false;
    }

    /**
     * Executes a command
     */
    @Override
    public void execute()
    {
        if(!canExecute())
        {
            return;
        }

        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeElement element = selectedView.getTreeElement();

        Board board = element.getBoard();
        int index = elementView.getIndex();

        if(element.getType() == TreeElementType.NODE)
        {
            TreeNode treeNode = (TreeNode) element;

            if(treeNode.getChildren().isEmpty())
            {
                if(transition == null)
                {
                    transition = tree.addNewTransition(treeNode);
                }
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
            }

            final TreeViewSelection newSelection = new TreeViewSelection(treeView.getElementView(transition));
            puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));

            board = transition.getBoard();

            data = board.getElementData(index);
            saveData = data.copy();
        }
        else
        {
            transition = (TreeTransition)element;
            data = board.getElementData(index);
            saveData = data.copy();
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        boardView.getElementController().changeCell(event, data);

        if(prevBoard.getElementData(index).equals(data))
        {
            board.removeModifiedData(data);
        }
        else
        {
            board.addModifiedData(data);
        }
        transition.propagateChanges(data);

        Board finalBoard = board;
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(finalBoard));
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(data));
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        TreeElementView selectedView = selection.getFirstSelection();
        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();
        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            if(!nodeView.getChildrenViews().isEmpty())
            {
                return false;
            }
            else
            {
                return board.getElementData(index).isModifiable();
            }
        }
        else
        {
            TreeTransitionView transitionView = (TreeTransitionView) selectedView;
            if(!transitionView.getTreeElement().getBoard().isModifiable())
            {
                return false;
            }
            else
            {
                return board.getElementData(index).isModifiable();
            }
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
        if(selection.getSelectedViews().size() > 1)
        {
            return "You have to have 1 tree view selected";
        }

        TreeElementView selectedView = selection.getFirstSelection();
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
        TreeElementView selectedView = selection.getFirstSelection();
        Tree tree = getInstance().getTree();
        Puzzle puzzle = getInstance().getPuzzleModule();

        Board board = transition.getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementRemoved(transition));
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        data.setValueInt(saveData.getValueInt());
        board.notifyChange(data);

        if(prevBoard.getElementData(index).equals(data))
        {
            board.removeModifiedData(data);
        }
        else
        {
            board.addModifiedData(data);
        }
        transition.propagateChanges(data);

        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(data));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
