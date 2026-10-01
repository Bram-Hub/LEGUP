package edu.rpi.legup.controller;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeElementView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;
import org.jetbrains.annotations.NotNull;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

/**
 * The {@code TreeController} class handles UI events from a {@code TreePanel}. It extends the
 * {@code Controller} class to provide specific behavior for tree interactions.
 */
public class TreeController extends ZoomViewController {

    /** {@code TreeController} constructor creates a controller object to listen to ui events from a {@link TreePanel}. */
    public TreeController() {}

    @Override
    public void mouseReleased(@NotNull ControllerMouseEvent e) {
        super.mouseReleased(e);

        TreeView treeView = (TreeView) getView();
        TreeElementView treeElementView = treeView.getTreeElementView(e.getPrecisePoint());
        Puzzle puzzle = getInstance().getPuzzleModule();
        TreeViewSelection selection = treeView.getSelection();

        if (treeElementView != null) {
            if (e.isShiftDown()) {
                selection.addToSelection(treeElementView);
            } else  if (e.isControlDown()) {
                selection.toggleSelection(treeElementView);
            } else {
                selection.newSelection(treeElementView);
            }

            puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
            puzzle.notifyBoardListeners(
                    listener -> listener.onTreeElementChanged(treeElementView.getTreeElement()));
        }
    }

    @Override
    public void mouseExited(@NotNull ControllerMouseEvent e) {
        TreeView treeView = (TreeView) getView();
        TreeViewSelection selection = treeView.getSelection();

        selection.clearHover();
    }

    @Override
    public void mouseDragged(@NotNull ControllerMouseEvent e) {
        TreeView treeView = (TreeView) getView();
        TreeElementView treeElementView = treeView.getTreeElementView(e.getPrecisePoint());
        Puzzle puzzle = getInstance().getPuzzleModule();

        if (puzzle != null) {
            TreeViewSelection selection = treeView.getSelection();
            selection.setMousePoint(e.getPrecisePoint());

            if (treeElementView != null && treeElementView != selection.getHover()) {
                puzzle.notifyBoardListeners(
                        listener -> listener.onTreeElementChanged(treeElementView.getTreeElement()));
                selection.newSelection(treeElementView);
                selection.newHover(treeElementView);
                puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
            }
            else if (treeElementView == null && selection.getHover() != null) {
                    puzzle.notifyBoardListeners(
                            listener ->
                                    listener.onTreeElementChanged(selection.getFirstSelection().getTreeElement()));
                    selection.clearHover();
            }
        }
    }

    @Override
    public void mouseMoved(@NotNull ControllerMouseEvent e) {
        TreeView treeView = (TreeView) getView();
        TreeElementView treeElementView = treeView.getTreeElementView(e.getPrecisePoint());
        Puzzle puzzle = getInstance().getPuzzleModule();

        if (puzzle != null) {
            TreeViewSelection selection = treeView.getSelection();
            selection.setMousePoint(e.getPrecisePoint());

            if (treeElementView != null && treeElementView != selection.getHover()) {
                selection.newHover(treeElementView);
                puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
            }
            else if (treeElementView == null && selection.getHover() != null) {
                selection.clearHover();
                puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
            }
        }
    }
}