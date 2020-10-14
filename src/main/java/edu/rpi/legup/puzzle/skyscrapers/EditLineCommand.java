package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.history.CommandError;
import edu.rpi.legup.history.PuzzleCommand;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.TreeElementView;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.awt.*;
import java.util.List;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class EditLineCommand extends PuzzleCommand {
    private SkyscrapersElementView start;
    private SkyscrapersElementView end;

    private TreeViewSelection selection;

    public EditLineCommand(TreeViewSelection selection, SkyscrapersElementView start, ElementView endDrag) {
        this.selection = selection;
        this.start = start;
        this.end = getViewInDirection(endDrag);
    }

    /**
     * Executes a command
     */
    @Override
    public void executeCommand() {
        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeElement treeElement = selectedView.getTreeElement();

        SkyscrapersBoard board = (SkyscrapersBoard) treeElement.getBoard();
        SkyscrapersCell startCell;
        SkyscrapersCell endCell;

        final TreeTransition transition;
        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) treeElement;

            transition = tree.addNewTransition(treeNode);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));

            board = (SkyscrapersBoard) transition.getBoard();
        } else {
            transition = (TreeTransition) treeElement;
        }

        startCell = (SkyscrapersCell) board.getPuzzleElement(start.getPuzzleElement());
        endCell = (SkyscrapersCell) board.getPuzzleElement(end.getPuzzleElement());

        SkyscrapersLine line = new SkyscrapersLine(startCell, endCell);

        SkyscrapersLine dupLine = null;
        for (SkyscrapersLine l : board.getLines()) {
            if (line.compare(l)) {
                dupLine = l;
                break;
            }
        }

        final SkyscrapersLine notifyLine;
        if (dupLine == null) {
            board.addModifiedData(line);
            board.getLines().add(line);
            notifyLine = line;
            transition.propagateAddition(notifyLine);
        } else {
            board.removeModifiedData(dupLine);
            board.getLines().remove(dupLine);
            notifyLine = dupLine;
            transition.propagateDeletion(notifyLine);
        }

        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(notifyLine));

        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(transition));

        final TreeViewSelection newSelection = new TreeViewSelection(treeView.getElementView(transition));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getErrorString() {
        List<TreeElementView> selectedViews = selection.getSelectedViews();
        if (selectedViews.size() != 1) {
            return CommandError.ONE_SELECTED_VIEW.toString();
        }

        if (start == null || end == null) {
            return "The line must connect a tree to a tent.";
        }

        TreeElementView view = selection.getFirstSelection();
        TreeElement treeElement = view.getTreeElement();
        SkyscrapersBoard board = (SkyscrapersBoard) treeElement.getBoard();
        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode node = (TreeNode) treeElement;
            if (!node.getChildren().isEmpty()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            }
        } else {
            if (!board.isModifiable()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            }
        }
        SkyscrapersLine line = new SkyscrapersLine((SkyscrapersCell) start.getPuzzleElement(), (SkyscrapersCell) end.getPuzzleElement());
        for (SkyscrapersLine l : board.getLines()) {
            if (line.compare(l) && !l.isModifiable()) {
                return CommandError.UNMODIFIABLE_DATA.toString();
            }
        }

        SkyscrapersCell startCell = (SkyscrapersCell) start.getPuzzleElement();
        SkyscrapersCell endCell = (SkyscrapersCell) end.getPuzzleElement();
        if (!((startCell.getType() == SkyscrapersType.TENT && endCell.getType() == SkyscrapersType.TREE) ||
                (endCell.getType() == SkyscrapersType.TENT && startCell.getType() == SkyscrapersType.TREE))) {
            return "The line must connect a tree to a tent.";
        }

        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeElement treeElement = selectedView.getTreeElement();

        SkyscrapersBoard board = (SkyscrapersBoard) treeElement.getBoard();
        SkyscrapersCell startCell;
        SkyscrapersCell endCell;
        TreeTransition transition;
        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) treeElement;
            transition = treeNode.getChildren().get(0);

            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));

            board = (SkyscrapersBoard) transition.getBoard();
        } else {
            transition = (TreeTransition) treeElement;
        }

        startCell = (SkyscrapersCell) board.getPuzzleElement(start.getPuzzleElement());
        endCell = (SkyscrapersCell) board.getPuzzleElement(end.getPuzzleElement());

        SkyscrapersLine line = new SkyscrapersLine(startCell, endCell);

        SkyscrapersLine dupLine = null;
        for (SkyscrapersLine l : board.getLines()) {
            if (line.compare(l)) {
                dupLine = l;
                break;
            }
        }

        final SkyscrapersLine notifyLine;
        if (dupLine == null) {
            board.addModifiedData(line);
            board.getLines().add(line);
            notifyLine = line;
        } else {
            board.removeModifiedData(dupLine);
            board.getLines().remove(dupLine);
            notifyLine = dupLine;
        }
        transition.propagateChange(notifyLine);
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(notifyLine));

        final TreeElement finalTreeElement = selection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));

        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }

    private SkyscrapersElementView getViewInDirection(ElementView endDrag) {
    	SkyscrapersView boardView = (SkyscrapersView) getInstance().getLegupUI().getBoardView();
        Dimension size = boardView.getElementSize();
        int xIndex, yIndex;

        Point startLoc = start.getLocation();
        Point endLoc = endDrag.getLocation();
        double radians = Math.atan2(startLoc.y - endLoc.y, endLoc.x - startLoc.x);
        if (radians >= Math.PI / 4 && radians < 3 * Math.PI / 4) {
            //up
            xIndex = startLoc.x / size.width;
            yIndex = (startLoc.y / size.height) - 1;
        } else if (radians >= -Math.PI / 4 && radians < Math.PI / 4) {
            //right
            xIndex = (startLoc.x / size.width) + 1;
            yIndex = startLoc.y / size.height;
        } else if (radians >= -3 * Math.PI / 4 && radians < -Math.PI / 4) {
            //down
            xIndex = startLoc.x / size.width;
            yIndex = (startLoc.y / size.height) + 1;
        } else {
            //left
            xIndex = (startLoc.x / size.width) - 1;
            yIndex = startLoc.y / size.height;
        }
        return (SkyscrapersElementView) boardView.getElement(xIndex - 1, yIndex - 1);
    }
}
