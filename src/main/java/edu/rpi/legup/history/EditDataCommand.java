package edu.rpi.legup.history;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.*;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class EditDataCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private boolean hasAddedNode;
    private PuzzleElement savePuzzleElement;
    private PuzzleElement puzzleElement;

    private ElementView elementView;
    private TreeViewSelection selection;
    private TreeElementView newSelectedView;
    private MouseEvent event;

    /**
     * EditDataCommand Constructor - create a edu.rpi.legup.puzzle command for editing a board
     *
     * @param elementView currently selected edu.rpi.legup.puzzle puzzleElement view that is being edited
     * @param selection currently selected tree puzzleElement views that is being edited
     * @param event mouse event
     */
    public EditDataCommand(ElementView elementView, TreeViewSelection selection, MouseEvent event)
    {
        this.elementView = elementView;
        this.selection = selection;
        this.event = event;
        this.puzzleElement = null;
        this.savePuzzleElement = null;
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
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();

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

            puzzleElement = board.getPuzzleElement(selectedPuzzleElement);
            savePuzzleElement = puzzleElement.copy();
        }
        else
        {
            transition = (TreeTransition)treeElement;
            puzzleElement = board.getPuzzleElement(selectedPuzzleElement);
            savePuzzleElement = this.puzzleElement.copy();
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        boardView.getElementController().changeCell(event, this.puzzleElement);

        if(prevBoard.getPuzzleElement(selectedPuzzleElement).equalsData(this.puzzleElement))
        {
            board.removeModifiedData(this.puzzleElement);
        }
        else
        {
            board.addModifiedData(this.puzzleElement);
        }
        transition.propagateChanges(this.puzzleElement);

        Board finalBoard = board;
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(finalBoard));
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(this.puzzleElement));
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        TreeElementView selectedView = selection.getFirstSelection();
        Board board = selectedView.getTreeElement().getBoard();
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            if(!nodeView.getChildrenViews().isEmpty())
            {
                return false;
            }
            else
            {
                return board.getPuzzleElement(selectedPuzzleElement).isModifiable();
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
                return board.getPuzzleElement(selectedPuzzleElement).isModifiable();
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

        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();
        if(!board.isModifiable())
        {
            return "Board is not modifiable";
        }
        else if(!board.getPuzzleElement(selectedPuzzleElement).isModifiable())
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
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementRemoved(transition));
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        puzzleElement.setData(savePuzzleElement.getData());
        board.notifyChange(puzzleElement);

        if(prevBoard.getPuzzleElement(selectedPuzzleElement).equalsData(puzzleElement))
        {
            board.removeModifiedData(puzzleElement);
        }
        else
        {
            board.addModifiedData(puzzleElement);
        }
        transition.propagateChanges(puzzleElement);

        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(puzzleElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
