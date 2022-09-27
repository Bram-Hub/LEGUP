package edu.rpi.legup.controller;

import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.ui.ScrollView;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.history.AutoCaseRuleCommand;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.ui.DynamicView;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementSelection;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.SelectionItemView;
import edu.rpi.legup.ui.proofeditorui.treeview.*;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.history.EditDataCommand;

import java.awt.*;
import java.awt.event.*;

import static edu.rpi.legup.app.GameBoardFacade.*;

public class ElementController implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
    protected BoardView boardView;
    private Element selectedElement;

    /**
     * ElementController Constructor controller to handles ui events associated interacting with a {@link BoardView}
     */
    public ElementController() {
        this.boardView = null;
        this.selectedElement = null;
    }

    public void setSelectedElement(Element selectedElement) {
        this.selectedElement = selectedElement;
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
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

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
                AutoCaseRuleCommand autoCaseRuleCommand = new AutoCaseRuleCommand(elementView, selection, caseBoard.getCaseRule(), caseBoard, e);
                if (autoCaseRuleCommand.canExecute()) {
                    autoCaseRuleCommand.execute();
                    getInstance().getHistory().pushChange(autoCaseRuleCommand);
                    if (treePanel != null) {
                        treePanel.updateError("");
                    }
                }
                else {
                    if (treePanel != null) {
                        treePanel.updateError(autoCaseRuleCommand.getError());
                    }
                }
            }
            else {
                if (selection != null) {
                    ICommand edit = new EditDataCommand(elementView, selection, e);
                    if (edit.canExecute()) {
                        edit.execute();
                        getInstance().getHistory().pushChange(edit);
                        if (treePanel != null) {
                            treePanel.updateError("");
                        }
                    }
                    else {
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
        Point scaledPoint = new Point((int) Math.floor(point.x / (30 * this.boardView.getScale())), (int) Math.floor(point.y / (30 * this.boardView.getScale())));
        if (this.boardView.getBoard() instanceof TreeTentBoard) {
            scaledPoint.setLocation(scaledPoint.getX() - 1, scaledPoint.getY() - 1);
        }
        System.out.printf("selected Element is NOT null, attempting to change board at (%d, %d)\n", scaledPoint.x, scaledPoint.y);
//            System.out.println("Before: " + b.getCell(scaledPoint.x, scaledPoint.y).getData());
        b.setCell(scaledPoint.x, scaledPoint.y, this.selectedElement, e);
//            System.out.println("After: " + b.getCell(scaledPoint.x, scaledPoint.y).getData());
//        } else {
//            System.out.println("selected Element is null!");
//        }
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
        TreeElement treeElement = boardView.getTreeElement();
        DynamicView dynamicView = getInstance().getLegupUI().getDynamicBoardView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        if (dynamicView == null) {
            dynamicView = getInstance().getLegupUI().getEditorDynamicBoardView();
        }
        Board board = boardView.getBoard();
        ElementView elementView = boardView.getElement(e.getPoint());
        ElementSelection selection = boardView.getSelection();
        String error = null;
        if (elementView != null) {
            selection.newHover(elementView);
            if (LegupPreferences.getInstance().getUserPrefAsBool(LegupPreferences.SHOW_MISTAKES)) {
                PuzzleElement element = elementView.getPuzzleElement();
                if (treeElement != null && treeElement.getType() == TreeElementType.TRANSITION && board.getModifiedData().contains(element)) {
                    TreeTransition transition = (TreeTransition) treeElement;
                    if (transition.isJustified() && !transition.isCorrect()) {
                        error = transition.getRule().checkRuleAt(transition, element);
                    }
                }
                if (error != null) {
                    dynamicView.updateError(error);
                }
                else {
                    dynamicView.resetStatus();
                }
            }
            boardView.repaint();
        }
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
        ElementView element = boardView.getElement(e.getPoint());
        if (element != null) {
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

    }

    /**
     * Invoked when the mouse moved
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        if (boardView == null) {
            boardView = getInstance().getLegupUI().getEditorBoardView();
        }
        Board board = boardView.getBoard();
        TreeElement treeElement = boardView.getTreeElement();
        DynamicView dynamicView = getInstance().getLegupUI().getDynamicBoardView();
        if (dynamicView == null) {
            dynamicView = getInstance().getLegupUI().getEditorDynamicBoardView();
        }
        ElementView elementView = boardView.getElement(e.getPoint());
        ElementSelection selection = boardView.getSelection();
        String error = null;
        if (elementView != null && elementView != selection.getHover()) {
            selection.newHover(elementView);
            if (LegupPreferences.getInstance().getUserPrefAsBool(LegupPreferences.SHOW_MISTAKES)) {
                PuzzleElement element = elementView.getPuzzleElement();
                if (treeElement != null && treeElement.getType() == TreeElementType.TRANSITION && board.getModifiedData().contains(element)) {
                    TreeTransition transition = (TreeTransition) treeElement;
                    if (transition.isJustified() && !transition.isCorrect()) {
                        error = transition.getRule().checkRuleAt(transition, element);
                    }
                }
                if (error != null) {
                    dynamicView.updateError(error);
                }
                else {
                    dynamicView.resetStatus();
                }
            }
            boardView.repaint();
        }
    }

    public void changeCell(MouseEvent e, PuzzleElement data) {

    }

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
        }
        else {
            puzzleElement.setModified(true);
        }

        transitionView.getTreeElement().propagateChange(puzzleElement);

        boardView.repaint();
        boardView.getSelection().clearSelection();
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
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
