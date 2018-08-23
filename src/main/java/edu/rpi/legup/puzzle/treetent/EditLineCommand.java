package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.*;
import edu.rpi.legup.history.PuzzleCommand;

import java.awt.*;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class EditLineCommand extends PuzzleCommand
{
    private TreeTentElementView start;
    private TreeTentElementView end;

    private TreeViewSelection selection;
    private TreeTransition transition;

    public EditLineCommand(TreeTentElementView start, TreeTentElementView endDrag, TreeViewSelection selection)
    {
        this.selection = selection;
        this.start = start;
        this.end = getViewInDirection(endDrag);
        this.transition = null;
    }

    /**
     * Executes a command
     */
    @Override
    public void executeCommand()
    {
        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeElement treeElement = selectedView.getTreeElement();

        TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
        TreeTentCell startCell;
        TreeTentCell endCell;

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

            board = (TreeTentBoard) transition.getBoard();
        }
        else
        {
            transition = (TreeTransition)treeElement;
        }

        startCell = (TreeTentCell) board.getPuzzleElement(start.getPuzzleElement());
        endCell = (TreeTentCell) board.getPuzzleElement(end.getPuzzleElement());

        TreeTentLine line = new TreeTentLine(startCell, endCell);

        TreeTentLine dupLine = null;
        for(TreeTentLine l : board.getLines()) {
            if(line.compare(l)) {
                dupLine = l;
                break;
            }
        }

        final TreeTentLine notifyLine;
        if(dupLine == null) {
            board.addModifiedData(line);
            board.getLines().add(line);
            notifyLine = line;
        } else {
            board.removeModifiedData(dupLine);
            board.getLines().remove(dupLine);
            notifyLine = dupLine;
        }

        Board finalBoard = board;
        puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(notifyLine));
        puzzle.notifyBoardListeners(listener -> listener.onBoardChanged(finalBoard));
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        if(start == null || end == null) {
            return false;
        }

        for(TreeElementView view : selection.getSelectedViews()) {
            TreeElement treeElement = view.getTreeElement();
            TreeTentBoard board = (TreeTentBoard)treeElement.getBoard();
            if(treeElement.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode)treeElement;
                if(!node.getChildren().isEmpty()) {
                    return false;
                }
            } else {
                if(!board.isModifiable()) {
                    return false;
                }
            }
            TreeTentLine line = new TreeTentLine((TreeTentCell)start.getPuzzleElement(), (TreeTentCell) end.getPuzzleElement());
            for(TreeTentLine l : board.getLines()) {
                if(line.compare(l) && !l.isModifiable()) {
                    return false;
                }
            }
        }

        TreeTentCell startCell = (TreeTentCell)start.getPuzzleElement();
        TreeTentCell endCell = (TreeTentCell)end.getPuzzleElement();
        if(!((startCell.getType() == TreeTentType.TENT && endCell.getType() == TreeTentType.TREE) ||
                (endCell.getType() == TreeTentType.TENT && startCell.getType() == TreeTentType.TREE))) {
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
        if(start == null || end == null) {
            return "You must connect a tree to a tent";
        }

        for(TreeElementView view : selection.getSelectedViews()) {
            TreeElement treeElement = view.getTreeElement();
            TreeTentBoard board = (TreeTentBoard)treeElement.getBoard();
            if(treeElement.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode)treeElement;
                if(!node.getChildren().isEmpty()) {
                    return "Board is not modifiable";
                }
            } else {
                if(!board.isModifiable()) {
                    return "Board is not modifiable";
                }
            }
            TreeTentLine line = new TreeTentLine((TreeTentCell)start.getPuzzleElement(), (TreeTentCell) end.getPuzzleElement());
            for(TreeTentLine l : board.getLines()) {
                if(line.compare(l) && !l.isModifiable()) {
                    return "Data is not modifiable";
                }
            }
        }

        TreeTentCell startCell = (TreeTentCell)start.getPuzzleElement();
        TreeTentCell endCell = (TreeTentCell)end.getPuzzleElement();
        if(!((startCell.getType() == TreeTentType.TENT && endCell.getType() == TreeTentType.TREE) ||
                (endCell.getType() == TreeTentType.TENT && startCell.getType() == TreeTentType.TREE))) {
            return "You must connect a tree to a tent";
        }

        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand()
    {

    }

    private TreeTentElementView getViewInDirection(TreeTentElementView endDrag) {
        TreeTentView boardView = (TreeTentView)getInstance().getLegupUI().getBoardView();
        Dimension size = boardView.getElementSize();
        int xIndex, yIndex;

        Point startLoc = start.getLocation();
        Point endLoc = endDrag.getLocation();
        double radians = Math.atan2(startLoc.y - endLoc.y, endLoc.x - startLoc.x);
        if(radians >= Math.PI / 4 && radians < 3 * Math.PI / 4) {
            //up
            xIndex = startLoc.x / size.width;
            yIndex = (startLoc.y / size.height) - 1;
        } else if(radians >= -Math.PI / 4 && radians < Math.PI / 4) {
            //right
            xIndex = (startLoc.x / size.width) + 1;
            yIndex = startLoc.y / size.height;
        } else if(radians >= -3 * Math.PI / 4 && radians < -Math.PI / 4) {
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
