package edu.rpi.legup.puzzle.treetent;

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
    private TreeTentElementView start;
    private TreeTentElementView end;

    private TreeViewSelection selection;

    public EditLineCommand(TreeViewSelection selection, TreeTentElementView start, ElementView endDrag) {
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

        TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
        TreeTentCell startCell;
        TreeTentCell endCell;

        final TreeTransition transition;
        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) treeElement;

            transition = tree.addNewTransition(treeNode);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));

            board = (TreeTentBoard) transition.getBoard();
        } else {
            transition = (TreeTransition) treeElement;
        }

        startCell = (TreeTentCell) board.getPuzzleElement(start.getPuzzleElement());
        endCell = (TreeTentCell) board.getPuzzleElement(end.getPuzzleElement());

        TreeTentLine line = new TreeTentLine(startCell, endCell);

        TreeTentLine dupLine = null;
        for (TreeTentLine l : board.getLines()) {
            if (line.compare(l)) {
                dupLine = l;
                break;
            }
        }

        final TreeTentLine notifyLine;
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
        TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
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
        TreeTentLine line = new TreeTentLine((TreeTentCell) start.getPuzzleElement(), (TreeTentCell) end.getPuzzleElement());
        for (TreeTentLine l : board.getLines()) {
            if (line.compare(l) && !l.isModifiable()) {
                return CommandError.UNMODIFIABLE_DATA.toString();
            }
        }

        TreeTentCell startCell = (TreeTentCell) start.getPuzzleElement();
        TreeTentCell endCell = (TreeTentCell) end.getPuzzleElement();
        if (!((startCell.getType() == TreeTentType.TENT && endCell.getType() == TreeTentType.TREE) ||
                (endCell.getType() == TreeTentType.TENT && startCell.getType() == TreeTentType.TREE))) {
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

        TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
        TreeTentCell startCell;
        TreeTentCell endCell;
        TreeTransition transition;
        if (treeElement.getType() == TreeElementType.NODE) {
            TreeNode treeNode = (TreeNode) treeElement;
            transition = treeNode.getChildren().get(0);

            tree.removeTreeElement(transition);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(transition));

            board = (TreeTentBoard) transition.getBoard();
        } else {
            transition = (TreeTransition) treeElement;
        }

        startCell = (TreeTentCell) board.getPuzzleElement(start.getPuzzleElement());
        endCell = (TreeTentCell) board.getPuzzleElement(end.getPuzzleElement());

        TreeTentLine line = new TreeTentLine(startCell, endCell);

        TreeTentLine dupLine = null;
        for (TreeTentLine l : board.getLines()) {
            if (line.compare(l)) {
                dupLine = l;
                break;
            }
        }

        final TreeTentLine notifyLine;
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

    private TreeTentElementView getViewInDirection(ElementView endDrag) {
        TreeTentView boardView = (TreeTentView) getInstance().getLegupUI().getBoardView();
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
        return (TreeTentElementView) boardView.getElement(xIndex - 1, yIndex - 1);
    }
}
