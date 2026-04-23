package edu.rpi.legup.controller;

import static edu.rpi.legup.app.GameBoardFacade.*;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.history.AutoCaseRuleCommand;
import edu.rpi.legup.history.EditDataCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.elements.PlaceableElement;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.ui.DynamicView;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementSelection;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.SelectionItemView;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The ElementController class manages UI interactions related to elements in a {@link BoardView}.
 * It handles mouse events, key events, and actions related to element selection and manipulation
 */
public class ElementController
        implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
    protected BoardView boardView;
    private Element selectedElement;
    private boolean goalPlacementMode;
    private GoalType currentGoalType;
    private PlaceableElement currentGoalValue;
    private Object goalValueData;
    private boolean assumeSolution;

    /**
     * ElementController Constructor controller to handle ui events associated interacting with a
     * {@link BoardView}
     */
    public ElementController() {
        this.boardView = null;
        this.selectedElement = null;
        this.goalPlacementMode = false;
        this.currentGoalType = GoalType.DEFAULT;
        this.currentGoalValue = null;
        this.goalValueData = null;
        this.assumeSolution = false;
    }

    /**
     * Sets the current selectedElement to the given selectedElement input
     *
     * @param selectedElement the {@link Element} to set as the currently selected element
     */
    public void setSelectedElement(Element selectedElement) {
        this.selectedElement = selectedElement;
    }

    public void setGoalPlacementMode(boolean goalPlacementMode) {
        this.goalPlacementMode = goalPlacementMode;
        if (goalPlacementMode) {
            this.selectedElement = null;
        }
    }

    public void setGoalType(GoalType goalType) {
        this.currentGoalType = goalType;
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (puzzle != null) {
            puzzle.setGoal(new Goal(goalType));
            // If the editor panel exists, update its displayed goal text
            if (GameBoardFacade.getInstance().getLegupUI() != null
                    && GameBoardFacade.getInstance().getLegupUI().getPuzzleEditor() != null) {
                GameBoardFacade.getInstance()
                        .getLegupUI()
                        .getPuzzleEditor()
                        .setGoalText(puzzle.getGoal().getGoalText());
            }
        }
    }

    public void setGoalValue(PlaceableElement selectedElement) {
        this.currentGoalValue = selectedElement;
    }

    public void setGoalValueData(Object valueData) {
        this.goalValueData = valueData;
    }

    public GoalType getCurrentGoalType() {
        return this.currentGoalType;
    }

    public PlaceableElement getCurrentGoalValue() {
        return this.currentGoalValue;
    }

    public void setAssumeSolution(boolean assume) {
        this.assumeSolution = assume;
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        puzzle.getGoal().setAssumeSolution(assume);
    }

    /**
     * Sets the {@link BoardView}
     *
     * @param boardView board view
     */
    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {}

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
        TreeView treeView = null;
        if (treePanel != null) {
            treeView = treePanel.getTreeView();
        }

        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }

        Board board = boardView.getBoard();
        ElementView elementView = boardView.getElement(e.getPoint());
        TreeViewSelection selection = null;
        if (treeView != null) {
            selection = treeView.getSelection();
        }
        // funny
        if (elementView != null) {
            if (board instanceof CaseBoard) {
                CaseBoard caseBoard = (CaseBoard) board;
                AutoCaseRuleCommand autoCaseRuleCommand =
                        new AutoCaseRuleCommand(
                                elementView, selection, caseBoard.getCaseRule(), caseBoard, e);
                if (autoCaseRuleCommand.canExecute()) {
                    autoCaseRuleCommand.execute();
                    getInstance().getHistory().pushChange(autoCaseRuleCommand);
                    if (treePanel != null) {
                        treePanel.updateError("");
                    }
                } else {
                    if (treePanel != null) {
                        treePanel.updateError(autoCaseRuleCommand.getError());
                    }
                }
            } else {
                if (selection != null) {
                    ICommand edit = new EditDataCommand(elementView, selection, e);
                    if (edit.canExecute()) {
                        edit.execute();
                        getInstance().getHistory().pushChange(edit);
                        if (treePanel != null) {
                            treePanel.updateError("");
                        }
                    } else {
                        if (treePanel != null) {
                            treePanel.updateError(edit.getError());
                        }
                    }
                }
            }
        }
        //        if (selectedElement != null) {
        GridBoard b = (GridBoard) this.boardView.getBoard();
        Point point = e.getPoint();
        Point scaledPoint =
                new Point(
                        (int) Math.floor(point.x / (30 * this.boardView.getScale())),
                        (int) Math.floor(point.y / (30 * this.boardView.getScale())));
        if (this.boardView.getBoard() instanceof TreeTentBoard) {
            scaledPoint.setLocation(scaledPoint.getX() - 1, scaledPoint.getY() - 1);
        }

        if (goalPlacementMode) {
            PuzzleElement selectedCell = b.getCell(scaledPoint.x, scaledPoint.y);
            if (selectedCell != null) {
                // Get the type of the goal value to determine how to set the cell's data and goal
                // data
                PlaceableElement element = (PlaceableElement) goalValueData;
                // If the element being placed isn't a number tile, simply toggle the cell's goal
                // status
                if (!element.toString().equals("Number Tile")) {
                    selectedCell.setGoal(!selectedCell.isGoal());
                }
                // If the element is a number tile and not already a goal cell, toggle the cell's
                // goal status
                // and set the goal data to the data value of the cell before placement
                else if (element.toString().equals("Number Tile") && !selectedCell.isGoal()) {
                    selectedCell.setGoal(!selectedCell.isGoal());
                    PuzzleElement tempCell = selectedCell.copy();
                    selectedCell.setGoalData(tempCell.getData());
                }
                if (selectedCell.isGoal() && goalValueData != null) {
                    // Create a temporary cell copy to determine what data value this element
                    // represents
                    PuzzleElement tempCell = selectedCell.copy();
                    if (element.toString().equals("Number Tile")) {
                        tempCell.setType(element, e);
                    } else {
                        selectedCell.setGoalData(tempCell.getData());
                        tempCell.setType(element, null);
                    }
                    // Set the cell's data to the goal value's data so the cell reflects the goal
                    // condition in the editor
                    selectedCell.setData(tempCell.getData());
                }
                // If the cell was already a goal and is being toggled off, clear the goal data and
                // reset the cell's data to the non-goal state
                else if (!selectedCell.isGoal()) {
                    selectedCell.setData(selectedCell.getGoalData());
                    selectedCell.setGoalData(null);
                }

                // Keep the Puzzle.goal cell list in sync with the cell's goal flag so
                // Goal.getHoverText(...) can find the goal cell when hovering.
                Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
                if (puzzle != null && selectedCell instanceof GridCell) {
                    GridCell<?> gridCell = (GridCell<?>) selectedCell;
                    Goal goal = puzzle.getGoal();
                    // If cell was just marked as a goal, add to goal list if not already present
                    if (selectedCell.isGoal()) {
                        boolean exists = false;
                        for (GridCell c : goal.getCells()) {
                            if (c.getLocation().equals(gridCell.getLocation())) {
                                exists = true;
                                // If cell is a number tile, update the goal cell in the goal list
                                // to have the correct data for hover text
                                if (element.toString().equals("Number Tile")) {
                                    goal.getCells().remove(c);
                                    goal.addCell(gridCell.copy());
                                }
                                break;
                            }
                        }
                        if (!exists) {
                            // Add a copy so Goal stores an independent snapshot of the goal cell
                            goal.addCell(gridCell.copy());
                        }

                    } else {
                        // If cell was unmarked as a goal, remove any matching location from the
                        // goal list
                        var itr = goal.getCells().iterator();
                        while (itr.hasNext()) {
                            GridCell c = itr.next();
                            if (c.getLocation().equals(gridCell.getLocation())) {
                                itr.remove();
                            }
                        }
                    }
                    // Update the editor's goal text so the UI reflects the new goal condition
                    if (GameBoardFacade.getInstance().getLegupUI() != null
                            && GameBoardFacade.getInstance().getLegupUI().getPuzzleEditor()
                                    != null) {
                        GameBoardFacade.getInstance()
                                .getLegupUI()
                                .getPuzzleEditor()
                                .setGoalText(goal.getGoalText());
                    }
                }
            }
        } else {
            b.setCell(scaledPoint.x, scaledPoint.y, this.selectedElement, e);
        }
        boardView.repaint();
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        boardView.setFocusable(true);
        boardView.requestFocusInWindow();
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        boardView.setFocusable(false);
        DynamicView dynamicView = getInstance().getLegupUI().getDynamicBoardView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        if (dynamicView == null) {
            dynamicView = getInstance().getLegupUI().getEditorDynamicBoardView();
        }
        if (boardView.getSelection().getHover() != null) {
            boardView.getSelection().clearHover();
            dynamicView.resetStatus();
            boardView.repaint();
        }
    }

    /**
     * Invoked when the mouse dragged
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        updateHover(e.getPoint());
    }

    /**
     * Invoked when the mouse moved
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        updateHover(e.getPoint());
    }

    /**
     * Updates which element the hover is being applied to.
     *
     * @param point Location of the cursor.
     */
    private void updateHover(Point point) {
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        DynamicView dynamicView = getInstance().getLegupUI().getDynamicBoardView();
        if (dynamicView == null) {
            dynamicView = getInstance().getLegupUI().getEditorDynamicBoardView();
        }
        Board board = boardView.getBoard();
        TreeElement treeElement = boardView.getTreeElement();
        ElementView elementView = boardView.getElement(point);
        ElementSelection selection = boardView.getSelection();
        String error = null;

        if (elementView != null && elementView != selection.getHover()) {
            selection.newHover(elementView);

            PuzzleElement cell = elementView.getPuzzleElement();
            if (cell instanceof GridCell && cell.isGoal()) {
                // Safely attempt to get hover text from the puzzle goal. Goal.getHoverText throws
                // IllegalArgumentException if the cell isn't present in the Goal's cell list, so
                // guard against that and any null puzzles.
                Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
                if (puzzle != null) {
                    try {
                        String hoverText = puzzle.getGoal().getHoverText((GridCell) cell);
                        boardView.getCanvas().setToolTipText(hoverText);
                    } catch (IllegalArgumentException | NullPointerException ex) {
                        // If goal data not found or something else is null, clear tooltip instead
                        boardView.getCanvas().setToolTipText(null);
                    }
                } else {
                    boardView.getCanvas().setToolTipText(null);
                }
            } else {
                boardView.getCanvas().setToolTipText(null);
            }

            if (LegupPreferences.showMistakes()) {
                PuzzleElement element = elementView.getPuzzleElement();
                if (treeElement != null
                        && treeElement.getType() == TreeElementType.TRANSITION
                        && board.getModifiedData().contains(element)) {
                    TreeTransition transition = (TreeTransition) treeElement;
                    if (transition.isJustified() && !transition.isCorrect()) {
                        error = transition.getRule().checkRuleAt(transition, element);
                    }
                }
                if (error != null) {
                    dynamicView.updateError(error);
                } else {
                    dynamicView.resetStatus();
                }
            }
            boardView.repaint();
        }
    }

    /**
     * Callback invoked when a cell is changed by a mouse event.
     * Intended to be overridden by subclasses to handle puzzle-specific
     * cell modification logic.
     *
     * @param e the mouse event that triggered the cell change
     * @param data the {@link PuzzleElement} representing the cell being changed
     */
    public void changeCell(MouseEvent e, PuzzleElement data) {}

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @SuppressWarnings("Unchecked")
    @Override
    public void actionPerformed(ActionEvent e) {
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        ElementView selectedElement = boardView.getSelection().getFirstSelection();
        PuzzleElement puzzleElement = selectedElement.getPuzzleElement();

        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeViewSelection selection = treeView.getSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeTransitionView transitionView = (TreeTransitionView) selectedView;

        Board prevBord = transitionView.getTreeElement().getParents().get(0).getBoard();

        int value = (Integer) ((SelectionItemView) e.getSource()).getData().getData();

        puzzleElement.setData(value);

        if (puzzleElement.equalsData(prevBord.getPuzzleElement(puzzleElement))) {
            puzzleElement.setModified(false);
        } else {
            puzzleElement.setModified(true);
        }

        transitionView.getTreeElement().propagateChange(puzzleElement);

        boardView.repaint();
        boardView.getSelection().clearSelection();
    }

    /**
     * Invoked when a key has been typed. See the class description for {@link KeyEvent} for a
     * definition of a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Invoked when a key has been pressed. See the class description for {@link KeyEvent} for a
     * definition of a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released. See the class description for {@link KeyEvent} for a
     * definition of a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        Board board = boardView.getBoard();
        if (board instanceof CaseBoard) {
            CaseBoard caseBoard = (CaseBoard) board;
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                puzzle.notifyBoardListeners(listener -> listener.onCaseBoardAdded(caseBoard));
            }
        }
    }
}
