package history;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.Element;
import model.observer.ITreeListener;
import model.tree.*;
import ui.boardview.BoardView;
import ui.boardview.ElementView;
import ui.treeview.*;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class EditDataCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private boolean hasAddedNode;
    private Element saveElement;
    private Element element;

    private ElementView elementView;
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
    public EditDataCommand(ElementView elementView, TreeViewSelection selection, MouseEvent event)
    {
        this.elementView = elementView;
        this.selection = selection;
        this.event = event;
        this.element = null;
        this.saveElement = null;
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
        TreeElement treeElement = selectedView.getTreeElement();

        Board board = treeElement.getBoard();
        Element selectedElement = elementView.getElement();

        if(treeElement.getType() == TreeElementType.NODE)
        {
            TreeNode treeNode = (TreeNode) treeElement;

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

            element = board.getElementData(selectedElement);
            saveElement = element.copy();
        }
        else
        {
            transition = (TreeTransition)treeElement;
            element = board.getElementData(selectedElement);
            saveElement = this.element.copy();
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        boardView.getElementController().changeCell(event, this.element);

        if(prevBoard.getElementData(selectedElement).equalsData(this.element))
        {
            board.removeModifiedData(this.element);
        }
        else
        {
            board.addModifiedData(this.element);
        }
        transition.propagateChanges(this.element);

        Board finalBoard = board;
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(finalBoard));
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(this.element));
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        TreeElementView selectedView = selection.getFirstSelection();
        Board board = selectedView.getTreeElement().getBoard();
        Element selectedElement = elementView.getElement();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            if(!nodeView.getChildrenViews().isEmpty())
            {
                return false;
            }
            else
            {
                return board.getElementData(selectedElement).isModifiable();
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
                return board.getElementData(selectedElement).isModifiable();
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

        Element selectedElement = elementView.getElement();
        if(!board.isModifiable())
        {
            return "Board is not modifiable";
        }
        else if(!board.getElementData(selectedElement).isModifiable())
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
        Element selectedElement = elementView.getElement();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementRemoved(transition));
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        element.setData(saveElement.getData());
        board.notifyChange(element);

        if(prevBoard.getElementData(selectedElement).equalsData(element))
        {
            board.removeModifiedData(element);
        }
        else
        {
            board.addModifiedData(element);
        }
        transition.propagateChanges(element);

        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(element));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
