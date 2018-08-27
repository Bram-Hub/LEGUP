package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.treeview.TreeElementView;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTreeElementCommand extends PuzzleCommand {

    private TreeViewSelection selection;

    private Map<TreeElement, TreeElement> addChild;

    /**
     * AddTreeElementCommand Constructor creates a command for adding a tree element to the proof tree
     *
     * @param selection selection of tree elements views
     */
    public AddTreeElementCommand(TreeViewSelection selection) {
        this.selection = selection.copy();
        this.addChild = new HashMap<>();
    }

    /**
     * Executes an command
     */
    @Override
    public void executeCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        List<TreeElementView> selectedViews = selection.getSelectedViews();
        for (TreeElementView view : selectedViews) {
            TreeElement treeElement = view.getTreeElement();
            TreeElement child = addChild.get(treeElement);
            if(child == null) {
                child = tree.addTreeElement(treeElement);
            } else {
                if (treeElement.getType() == TreeElementType.NODE) {
                    child = tree.addTreeElement((TreeNode)treeElement, (TreeTransition)child);
                } else {
                    child = tree.addTreeElement((TreeTransition) treeElement, (TreeNode) child);
                }
            }
            addChild.put(treeElement, child);

            final TreeElement finalChild = child;
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(finalChild));

            newSelection.addToSelection(treeView.getElementView(child));
        }

        final TreeElement finalTreeElement = newSelection.getFirstSelection().getTreeElement();
        puzzle.notifyBoardListeners(listener -> listener.onTreeElementChanged(finalTreeElement));
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
        if (selectedViews.isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
        } else {
            for (TreeElementView view : selectedViews) {
                TreeElement element = view.getTreeElement();
                if (element.getType() == TreeElementType.TRANSITION) {
                    TreeTransition transition = (TreeTransition) element;
                    if (transition.getChildNode() != null) {
                        return CommandError.ADD_WITH_CHILD.toString();
                    }
                } else {
                    TreeNode node = (TreeNode)element;
                    if(!node.getChildren().isEmpty()) {
                        TreeTransition transition = node.getChildren().get(0);
                        if(transition.getParents().size() > 1) {
                            return CommandError.ADD_TO_MERGE.toString();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undoCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        final TreeViewSelection newSelection = new TreeViewSelection();

        for (TreeElementView view : selection.getSelectedViews()) {
            TreeElement element = view.getTreeElement();
            TreeElement child = addChild.get(element);
            tree.removeTreeElement(child);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(child));
            newSelection.addToSelection(treeView.getElementView(element));
        }
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(newSelection));
    }
}