package edu.rpi.legup.controller;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.*;

public class TreeController extends Controller {
  /**
   * TreeController Constructor creates a controller object to listen to ui events from a {@link
   * TreePanel}
   */
  public TreeController() {}

  /**
   * Mouse Clicked event no default action
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseClicked(MouseEvent e) {}

  /**
   * Mouse Pressed event sets the cursor to the move cursor and stores info for possible panning
   *
   * @param e MouseEvent object
   */
  @Override
  public void mousePressed(MouseEvent e) {
    super.mousePressed(e);
  }

  /**
   * Mouse Released event sets the cursor back to the default cursor and reset info for panning Set
   * board modifiability
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    super.mouseReleased(e);

    TreeView treeView = (TreeView) viewer;
    Point point = treeView.getActualPoint(e.getPoint());
    TreeElementView treeElementView = treeView.getTreeElementView(point);
    Puzzle puzzle = getInstance().getPuzzleModule();
    TreeViewSelection selection = treeView.getSelection();
    if (treeElementView != null) {
      if (e.isShiftDown()) {
        selection.addToSelection(treeElementView);
      } else {
        if (e.isControlDown()) {
          if (!(selection.getSelectedViews().size() == 1
              && treeElementView == selection.getFirstSelection())) {
            selection.toggleSelection(treeElementView);
          }
        } else {
          selection.newSelection(treeElementView);
        }
      }
      puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
      puzzle.notifyBoardListeners(
          listener -> listener.onTreeElementChanged(treeElementView.getTreeElement()));
    }
  }

  /**
   * Mouse Entered event no default action
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    TreeView treeView = (TreeView) viewer;
    Point point = treeView.getActualPoint(e.getPoint());
    Tree tree = getInstance().getTree();
    BoardView boardView = getInstance().getLegupUI().getBoardView();
    TreeElementView treeElementView = treeView.getTreeElementView(point);
    Puzzle puzzle = getInstance().getPuzzleModule();
    if (treeElementView != null) {
      puzzle.notifyBoardListeners(
          listener -> listener.onTreeElementChanged(treeElementView.getTreeElement()));
    }
  }

  /**
   * Mouse Exited event no default action
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseExited(MouseEvent e) {
    TreeView treeView = (TreeView) viewer;
    Point point = treeView.getActualPoint(e.getPoint());
    TreeElementView elementView = treeView.getTreeElementView(point);
    Puzzle puzzle = getInstance().getPuzzleModule();
    TreeViewSelection selection = treeView.getSelection();

    selection.setMousePoint(null);
    if (elementView != null) {
      TreeElementView selectedView = selection.getFirstSelection();
      puzzle.notifyBoardListeners(
          listener -> listener.onTreeElementChanged(selectedView.getTreeElement()));
    }
  }

  /**
   * Mouse Dragged event adjusts the viewport
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    super.mouseDragged(e);
  }

  /**
   * Mouse Moved event no default action
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    TreeView treeView = (TreeView) viewer;
    Point point = treeView.getActualPoint(e.getPoint());
    TreeElementView treeElementView = treeView.getTreeElementView(point);
    Puzzle puzzle = getInstance().getPuzzleModule();
    if (puzzle != null) {
      TreeViewSelection selection = treeView.getSelection();
      selection.setMousePoint(treeView.getActualPoint(e.getPoint()));
      if (treeElementView != null && treeElementView != selection.getHover()) {
        puzzle.notifyBoardListeners(
            listener -> listener.onTreeElementChanged(treeElementView.getTreeElement()));
        selection.newHover(treeElementView);
        puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
      } else {
        if (treeElementView == null && selection.getHover() != null) {
          puzzle.notifyBoardListeners(
              listener ->
                  listener.onTreeElementChanged(selection.getFirstSelection().getTreeElement()));
          selection.clearHover();
          puzzle.notifyTreeListeners(listener -> listener.onTreeSelectionChanged(selection));
        }
      }
    }
  }

  /**
   * Mouse Wheel Moved event zooms in on the viewport
   *
   * @param e MouseEvent object
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    super.viewer.scroll(e.getWheelRotation());
  }
}
