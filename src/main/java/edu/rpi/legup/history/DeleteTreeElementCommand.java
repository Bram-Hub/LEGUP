package edu.rpi.legup.history;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.util.List;

public class DeleteTreeElementCommand extends PuzzleCommand {
    private TreeViewSelection selection;

    /**
     * DeleteTreeElementCommand Constructor creates a PuzzleCommand for deleting a tree
     * puzzleElement
     *
     * @param selection the currently selected tree elements before the command is executed
     */
    public DeleteTreeElementCommand(TreeViewSelection selection) {
        this.selection = selection.copy();
    }

    /** Executes an command */
    @Override
    public void executeCommand() {
        Tree tree = GameBoardFacade.getInstance().getTree();
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();

        List<TreeElementView> selectedViews = selection.getSelectedViews();

        TreeElementView firstSelectedView = selectedViews.get(0);
        TreeElementView newSelectedView;
        if (firstSelectedView.getType() == TreeElementType.NODE) {
            TreeNodeView nodeView = (TreeNodeView) firstSelectedView;
            newSelectedView = nodeView.getParentView();
        } else {
            TreeTransitionView transitionView = (TreeTransitionView) firstSelectedView;
            newSelectedView = transitionView.getParentViews().get(0);
        }

        for (TreeElementView selectedView : selectedViews) {
            TreeElement element = selectedView.getTreeElement();
            tree.removeTreeElement(element);
            puzzle.notifyTreeListeners(listener -> listener.onTreeElementRemoved(element));
        }

        final TreeViewSelection newSelection = new TreeViewSelection(newSelectedView);
        puzzle.notifyBoardListeners(
                listener -> listener.onTreeElementChanged(newSelectedView.getTreeElement()));
        puzzle.notifyTreeListeners(
                (ITreeListener listener) -> listener.onTreeSelectionChanged(newSelection));
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
        if (selectedViews.isEmpty()) {
            return CommandError.NO_SELECTED_VIEWS.toString();
        }

        for (TreeElementView view : selectedViews) {
            TreeElement element = view.getTreeElement();
            if (element.getType() == TreeElementType.NODE && ((TreeNode) element).isRoot()) {
                return CommandError.CONTAINS_ROOT.toString();
            }
        }
        return null;
    }

    /** Undoes an command */
    @Override
    public void undoCommand() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        List<TreeElementView> selectedViews = selection.getSelectedViews();

        for (TreeElementView selectedView : selectedViews) {
            TreeElement element = selectedView.getTreeElement();
            if (element.getType() == TreeElementType.NODE) {
                TreeNode node = (TreeNode) element;
                node.getParent().setChildNode(node);

                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(node));
            } else {
                TreeTransition transition = (TreeTransition) element;
                transition.getParents().forEach(node -> node.addChild(transition));
                transition.getParents().get(0).getChildren().forEach(TreeTransition::reverify);

                puzzle.notifyTreeListeners(listener -> listener.onTreeElementAdded(transition));
            }
        }

        puzzle.notifyBoardListeners(
                listener ->
                        listener.onTreeElementChanged(
                                selection.getFirstSelection().getTreeElement()));
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
    }
}
