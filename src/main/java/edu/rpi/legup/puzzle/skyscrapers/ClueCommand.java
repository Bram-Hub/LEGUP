package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.history.CommandError;
import edu.rpi.legup.history.PuzzleCommand;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeNodeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeTransitionView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueCommand extends PuzzleCommand {
    private TreeViewSelection selection;
    private SkyscrapersClueView clueView;
    private Map<TreeNode, TreeTransition> addTran;
    private List<List<SkyscrapersCell>> emptyCells;

    public ClueCommand(TreeViewSelection selection, SkyscrapersClueView clueView) {
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
        /*Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();

        final TreeViewSelection newSelection = new TreeViewSelection();
        for (int i = 0; i < selection.getSelectedViews().size(); i++) {
            TreeElementView selectedView = selection.getSelectedViews().get(i);
            TreeElement treeElement = selectedView.getTreeElement();

            final TreeTransition finalTran;
            SkyscrapersBoard board = (SkyscrapersBoard) treeElement.getBoard();
            List<SkyscrapersCell> tempList = emptyCells.get(i);
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
                board = (SkyscrapersBoard) finalTran.getBoard();
            } else {
                finalTran = (TreeTransition) treeElement;
                newSelection.addToSelection(treeView.getElementView(treeElement));
            }

            for (SkyscrapersCell cell : tempList) {
                cell = (SkyscrapersCell) board.getPuzzleElement(cell);
                cell.setData(SkyscrapersType.GRASS.value);
                board.addModifiedData(cell);
                finalTran.propagateChange(cell);

                final SkyscrapersCell finalCell = cell;
                puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(finalCell));
            }
            if (i == 0) {
                puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTran));
            }
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));*/
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
        TreeElementView selectedView = selection.getFirstSelection();
        Board board = selectedView.getTreeElement().getBoard();
        SkyscrapersClue selectedPuzzleElement = clueView.getPuzzleElement();
        if (selectedView.getType() == TreeElementType.NODE) {
        	
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            if (!nodeView.getChildrenViews().isEmpty()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            } else {
                if (!board.getPuzzleElement(selectedPuzzleElement).isModifiable()) {
                    return CommandError.UNMODIFIABLE_DATA.toString();
                }
            }
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) selectedView;
            if (!transitionView.getTreeElement().getBoard().isModifiable()) {
                return CommandError.UNMODIFIABLE_BOARD.toString();
            } else {
                if (!board.getPuzzleElement(selectedPuzzleElement).isModifiable()) {
                    return CommandError.UNMODIFIABLE_DATA.toString();
                }
            }
        }
        return null;
    }
    /*@Override
    public String getErrorString() {
        if (selection.getSelectedViews().isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
        }

        emptyCells.clear();
        for (TreeElementView view : selection.getSelectedViews()) {
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

            List<SkyscrapersCell> tempList = new ArrayList<>();
            SkyscrapersClue clue = clueView.getPuzzleElement();
            if (clue.getType() == SkyscrapersType.CLUE_NORTH || clue.getType() == SkyscrapersType.CLUE_SOUTH) {
                int col = clue.getType() == SkyscrapersType.CLUE_NORTH ? clue.getClueIndex() : clue.getClueIndex() - 1;
                for (int i = 0; i < board.getWidth(); i++) {
                	SkyscrapersCell cell = board.getCell(col, i);
                    if (cell.getType() == SkyscrapersType.UNKNOWN && cell.isModifiable()) {
                        tempList.add(cell);
                    }
                }
            } else {
                int row = clue.getType() == SkyscrapersType.CLUE_WEST ? clue.getClueIndex() : clue.getClueIndex() - 1;
                for (int i = 0; i < board.getWidth(); i++) {
                	SkyscrapersCell cell = board.getCell(i, row);
                    if (cell.getType() == SkyscrapersType.UNKNOWN && cell.isModifiable()) {
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
    }*/

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        /*Puzzle puzzle = getInstance().getPuzzleModule();
        Tree tree = puzzle.getTree();

        for (int i = 0; i < selection.getSelectedViews().size(); i++) {
            TreeElementView selectedView = selection.getSelectedViews().get(i);
            TreeElement treeElement = selectedView.getTreeElement();

            final TreeTransition finalTran;
            SkyscrapersBoard board = (SkyscrapersBoard) treeElement.getBoard();
            List<SkyscrapersCell> tempList = emptyCells.get(i);
            if (treeElement.getType() == TreeElementType.NODE) {
                TreeNode treeNode = (TreeNode) treeElement;

                finalTran = treeNode.getChildren().get(0);
                tree.removeTreeElement(finalTran);
                puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(finalTran));

                board = (SkyscrapersBoard) finalTran.getBoard();
            } else {
                finalTran = (TreeTransition) treeElement;
            }

            for (SkyscrapersCell cell : tempList) {
                cell = (SkyscrapersCell) board.getPuzzleElement(cell);
                cell.setData(SkyscrapersType.UNKNOWN.value);
                board.removeModifiedData(cell);

                final SkyscrapersCell finalCell = cell;
                puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(finalCell));
            }

            if (i == 0) {
                puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTran));
            }
        }
        final TreeViewSelection newSelection = selection;
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));*/
    }
}
