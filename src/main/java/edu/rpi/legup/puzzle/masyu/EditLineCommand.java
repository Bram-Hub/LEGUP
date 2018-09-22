package edu.rpi.legup.puzzle.masyu;

import edu.rpi.legup.history.PuzzleCommand;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.*;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class EditLineCommand extends PuzzleCommand {
    private TreeTransition transition;
    private PuzzleElement oldData;
    private PuzzleElement newData;

    private ElementView elementView;
    private TreeElementView selectedView;
    private TreeElementView newSelectedView;
    private MouseEvent event;

    private TreeTransitionView transitionView;

    public EditLineCommand(ElementView elementView, TreeElementView selectedView, MouseEvent event, MasyuLine line) {
        this.elementView = elementView;
        this.selectedView = selectedView;
        this.event = event;
        this.newData = line;
        this.oldData = newData.copy();
        this.transition = null;
    }

    /**
     * Executes a command
     */
    @Override
    public void executeCommand() {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        Puzzle puzzle = getInstance().getPuzzleModule();

        MasyuBoard board = (MasyuBoard) selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();

        if (selectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            if (transition == null) {
                transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
            }

            treeNode.getChildren().add(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(transition));
            transitionView = (TreeTransitionView) treeView.getElementView(transition);

            selection.newSelection(transitionView);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeSelectionChanged(selection));

            getInstance().getLegupUI().repaintTree();
            board = (MasyuBoard) transition.getBoard();
            getInstance().getPuzzleModule().setCurrentBoard(board);
            oldData = newData.copy();
        } else {
            transitionView = (TreeTransitionView) selectedView;
            transition = transitionView.getTreeElement();
        }
        newSelectedView = transitionView;
        PuzzleElement dup_line = null;
        boolean mod_contains = false;
        boolean contains = false;
        final MasyuBoard editBoard = board;
        System.out.println("Size: " + board.getModifiedData().size());
        for (PuzzleElement puzzleElement : board.getModifiedData()) {
            if (puzzleElement instanceof MasyuLine) {
                if (((MasyuLine) newData).compare((MasyuLine) puzzleElement)) {
                    System.out.println("contains");
                    dup_line = puzzleElement;
                    mod_contains = true;
                }
            }
        }
        for (int i = 0; i < board.getLines().size(); i++) {
            if (board.getLines().get(i).compare((MasyuLine) newData)) {
                contains = true;
            }
        }
        if (contains || mod_contains) {
            System.out.println("delete");
            board.getModifiedData().remove(dup_line);
            board.getLines().remove(dup_line);
//            puzzle.notifyBoardListeners((IBoardListener listener) -> listener.onTreeElementChanged(editBoard));
        } else {
            System.out.println("adding");
            board.getModifiedData().add(newData);
            board.getLines().add((MasyuLine) newData);
//            puzzle.notifyBoardListeners((IBoardListener listener) -> listener.onTreeElementChanged(editBoard));
        }

        transition.propagateChange(newData);
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        Board board = selectedView.getTreeElement().getBoard();
        if (!board.isModifiable()) {
            return "Board is not modifiable";
        } else if (!board.getPuzzleElement(elementView.getPuzzleElement()).isModifiable()) {
            return "Data is not modifiable";
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();

        Board board = transition.getBoard();

        if (selectedView.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            tree.removeTreeElement(transition);
            treeView.removeTreeElement(newSelectedView);

            selection.newSelection(selectedView);

            getInstance().getLegupUI().repaintTree();
            getInstance().getPuzzleModule().setCurrentBoard(treeNode.getBoard());
        }

        Board prevBoard = null;// transition.getParentNode().getBoard();

        newData.setData(oldData.getData());
        board.notifyChange(newData);

        //System.err.println(newData.getData() + " : " + oldData.getData());

        if (prevBoard.getPuzzleElement(elementView.getPuzzleElement()).equalsData(newData)) {
            board.removeModifiedData(newData);
        } else {
            board.addModifiedData(newData);
        }
        transition.propagateChange(newData);
    }
}
