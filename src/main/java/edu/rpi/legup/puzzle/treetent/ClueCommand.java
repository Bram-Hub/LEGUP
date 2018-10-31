package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.history.CommandError;
import edu.rpi.legup.history.PuzzleCommand;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.TreeElementView;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class ClueCommand extends PuzzleCommand {
    private TreeViewSelection selection;
    private TreeTentClueView clueView;
    private Map<TreeNode, TreeTransition> addTran;
    private List<List<TreeTentCell>> emptyCells;

    public ClueCommand(TreeViewSelection selection, TreeTentClueView clueView) {
        this.selection = selection;
        this.clueView = clueView;
        this.addTran = new HashMap<>();
        this.emptyCells = new ArrayList<>();
    }

    /**
     * Executes a command
     */
    @Override
    public void executeCommand() {
        Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();

        final TreeViewSelection newSelection = new TreeViewSelection();
        for (int i = 0; i < selection.getSelectedViews().size(); i++) {
            TreeElementView selectedView = selection.getSelectedViews().get(i);
            TreeElement treeElement = selectedView.getTreeElement();

            final TreeTransition finalTran;
            TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
            List<TreeTentCell> tempList = emptyCells.get(i);
            if (treeElement.getType() == TreeElementType.NODE) {
                TreeNode treeNode = (TreeNode) treeElement;

                TreeTransition transition = addTran.get(treeNode);
                if (transition == null) {
                    transition = tree.addNewTransition(treeNode);
                    addTran.put(treeNode, transition);
                } else {
                    treeNode.addChild(transition);
                }

                finalTran = transition;
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalTran));

                newSelection.addToSelection(treeView.getElementView(finalTran));
                board = (TreeTentBoard) finalTran.getBoard();
            } else {
                finalTran = (TreeTransition) treeElement;
                newSelection.addToSelection(treeView.getElementView(treeElement));
            }

            for (TreeTentCell cell : tempList) {
                cell = (TreeTentCell) board.getPuzzleElement(cell);
                cell.setData(TreeTentType.GRASS.value);
                board.addModifiedData(cell);
                finalTran.propagateChange(cell);

                final TreeTentCell finalCell = cell;
                puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(finalCell));
            }
            if (i == 0) {
                puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTran));
            }
        }
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
        if (selection.getSelectedViews().isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
        }

        emptyCells.clear();
        for (TreeElementView view : selection.getSelectedViews()) {
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

            List<TreeTentCell> tempList = new ArrayList<>();
            TreeTentClue clue = clueView.getPuzzleElement();
            if (clue.getType() == TreeTentType.CLUE_NORTH || clue.getType() == TreeTentType.CLUE_SOUTH) {
                int col = clue.getType() == TreeTentType.CLUE_NORTH ? clue.getClueIndex() : clue.getClueIndex() - 1;
                for (int i = 0; i < board.getWidth(); i++) {
                    TreeTentCell cell = board.getCell(col, i);
                    if (cell.getType() == TreeTentType.UNKNOWN && cell.isModifiable()) {
                        tempList.add(cell);
                    }
                }
            } else {
                int row = clue.getType() == TreeTentType.CLUE_WEST ? clue.getClueIndex() : clue.getClueIndex() - 1;
                for (int i = 0; i < board.getWidth(); i++) {
                    TreeTentCell cell = board.getCell(i, row);
                    if (cell.getType() == TreeTentType.UNKNOWN && cell.isModifiable()) {
                        tempList.add(cell);
                    }
                }
            }
            if (tempList.isEmpty()) {
                return "There are no modifiable unknown cells in every selected tree element.";
            }
            emptyCells.add(tempList);
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

        for (int i = 0; i < selection.getSelectedViews().size(); i++) {
            TreeElementView selectedView = selection.getSelectedViews().get(i);
            TreeElement treeElement = selectedView.getTreeElement();

            final TreeTransition finalTran;
            TreeTentBoard board = (TreeTentBoard) treeElement.getBoard();
            List<TreeTentCell> tempList = emptyCells.get(i);
            if (treeElement.getType() == TreeElementType.NODE) {
                TreeNode treeNode = (TreeNode) treeElement;

                finalTran = treeNode.getChildren().get(0);
                tree.removeTreeElement(finalTran);
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(finalTran));

                board = (TreeTentBoard) finalTran.getBoard();
            } else {
                finalTran = (TreeTransition) treeElement;
            }

            for (TreeTentCell cell : tempList) {
                cell = (TreeTentCell) board.getPuzzleElement(cell);
                cell.setData(TreeTentType.UNKNOWN.value);
                board.removeModifiedData(cell);

                final TreeTentCell finalCell = cell;
                puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(finalCell));
            }

            if (i == 0) {
                puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTran));
            }
        }
        final TreeViewSelection newSelection = selection;
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }
}
