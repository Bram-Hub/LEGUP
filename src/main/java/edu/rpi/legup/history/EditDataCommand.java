package edu.rpi.legup.history;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;

/**
 * The EditDataCommand class represents a command to edit the data of a puzzle element within a tree
 * transition. It extends PuzzleCommand and provides functionality to execute and undo changes made
 * to puzzle elements.
 */
public class EditDataCommand extends PuzzleCommand {
    private TreeTransition transition;
    private PuzzleElement savePuzzleElement;
    private PuzzleElement puzzleElement;

    private ElementView elementView;
    private TreeViewSelection selection;
    private MouseEvent event;

    /**
     * EditDataCommand Constructor create a puzzle command for editing a board
     *
     * @param elementView currently selected puzzle puzzleElement view that is being edited
     * @param selection currently selected tree puzzleElement views that are being edited
     * @param event mouse event
     */
    public EditDataCommand(ElementView elementView, TreeViewSelection selection, MouseEvent event) {
        this.elementView = elementView;
        this.selection = selection.copy();
        this.event = event;
        this.puzzleElement = null;
        this.savePuzzleElement = null;
        this.transition = null;
    }

    /** Executes the edit data command, modifying the puzzle element and propagating changes */
    @SuppressWarnings("unchecked")
    @Override
    public void executeCommand() {
        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeElement treeElement = selectedView.getTreeElement();

        Board board = treeElement.getBoard();
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();

        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) treeElement;
            if (treeNode.getChildren().isEmpty()) {
                if (transition == null) {
                    transition = tree.addNewTransition(treeNode);
                }
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
            }
            board = transition.getBoard();
            puzzleElement = board.getPuzzleElement(selectedPuzzleElement);
            savePuzzleElement = puzzleElement.copy();
        } else {
            transition = (TreeTransition) treeElement;
            puzzleElement = board.getPuzzleElement(selectedPuzzleElement);
            savePuzzleElement = puzzleElement.copy();
        }

        Board prevBoard = transition.getParents().get(0).getBoard();
        boardView.getElementController().changeCell(event, puzzleElement);

        if (prevBoard.getPuzzleElement(selectedPuzzleElement).equalsData(puzzleElement)) {
            board.removeModifiedData(puzzleElement);
        } else {
            board.addModifiedData(puzzleElement);
        }
        transition.propagateChange(puzzleElement);

        final TreeElement finalTreeElement = transition;
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(puzzleElement));

        final TreeViewSelection newSelection =
                new TreeViewSelection(treeView.getElementView(transition));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     *     otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        List<TreeElementView> selectedViews = selection.getSelectedViews();
        if (selectedViews.size() != 1) {
            return CommandError.ONE_SELECTED_VIEW.toString();
        }
        TreeElementView selectedView = selection.getFirstSelection();
        Board board = selectedView.getTreeElement().getBoard();
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();
        if (selectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            if (!nodeView.getChildrenViews().isEmpty()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            } else if (!board.getPuzzleElement(selectedPuzzleElement).isModifiable()) {
                return CommandError.UNMODIFIABLE_DATA.toString();
            }
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) selectedView;
            if (!transitionView.getTreeElement().getBoard().isModifiable()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            } else {
                if (!board.getPuzzleElement(selectedPuzzleElement).isModifiable()) {
                    return CommandError.UNMODIFIABLE_DATA.toString();
                } else if (!board.getPuzzleElement(selectedPuzzleElement).isModifiableCaseRule()) {
                    return CommandError.UNMODIFIABLE_DATA_CASE_RULE.toString();
                }
            }
        }
        return null;
    }

    /** Undoes the edit data command, restoring the previous state of the puzzle element. */
    @SuppressWarnings("unchecked")
    @Override
    public void undoCommand() {
        TreeElementView selectedView = selection.getFirstSelection();
        Tree tree = getInstance().getTree();
        Puzzle puzzle = getInstance().getPuzzleModule();

        Board board = transition.getBoard();
        PuzzleElement selectedPuzzleElement = elementView.getPuzzleElement();

        if (selectedView.getType() == TreeElementType.NODE) {
            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners(
                    (ITreeListener listener) -> listener.onTreeElementRemoved(transition));
        }

        Board prevBoard = transition.getParents().get(0).getBoard();

        puzzleElement.setData(savePuzzleElement.getData());
        board.notifyChange(puzzleElement);

        if (prevBoard.getPuzzleElement(selectedPuzzleElement).equalsData(puzzleElement)) {
            board.removeModifiedData(puzzleElement);
        } else {
            board.addModifiedData(puzzleElement);
        }
        transition.propagateChange(puzzleElement);

        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(puzzleElement));

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
