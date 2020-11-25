package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.history.AutoCaseRuleCommand;
import edu.rpi.legup.history.EditDataCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.TreePanel;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class SkyscrapersController extends ElementController {

    private ElementView lastCellPressed;
    private ElementView dragStart;

    public SkyscrapersController() {
        super();
        this.dragStart = null;
        this.lastCellPressed = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON2) {
            BoardView boardView = getInstance().getLegupUI().getBoardView();
            dragStart = boardView.getElement(e.getPoint());
            lastCellPressed = boardView.getElement(e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON2) {
            TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
            TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
            BoardView boardView = getInstance().getLegupUI().getBoardView();
            lastCellPressed = boardView.getElement(e.getPoint());
            Board board = boardView.getBoard();
            TreeViewSelection selection = treeView.getSelection();

            if (dragStart != null) {
                if (board instanceof CaseBoard) {
                    CaseBoard caseBoard = (CaseBoard) board;
                    AutoCaseRuleCommand autoCaseRuleCommand = new AutoCaseRuleCommand(dragStart, selection, caseBoard.getCaseRule(), caseBoard, e);
                    if (autoCaseRuleCommand.canExecute()) {
                        autoCaseRuleCommand.execute();
                        getInstance().getHistory().pushChange(autoCaseRuleCommand);
                        treePanel.updateError("");
                    } else {
                        treePanel.updateError(autoCaseRuleCommand.getError());
                    }
                } else {
                    if (dragStart == lastCellPressed) {
                        if (dragStart.getPuzzleElement().getIndex() >= 0) {
                            ICommand edit = new EditDataCommand(lastCellPressed, selection, e);
                            if (edit.canExecute()) {
                                edit.execute();
                                getInstance().getHistory().pushChange(edit);
                                treePanel.updateError("");
                            } else {
                                treePanel.updateError(edit.getError());
                            }
                        } else {
                            ClueCommand edit = new ClueCommand(selection, (SkyscrapersClueView) dragStart);
                            if (edit.canExecute()) {
                                edit.execute();
                                getInstance().getHistory().pushChange(edit);
                                treePanel.updateError("");
                            } else {
                                treePanel.updateError(edit.getError());
                            }
                        }
                    } /*else if (lastCellPressed != null) {
                        if (dragStart instanceof SkyscrapersElementView) {
                            ICommand editLine = new EditLineCommand(selection, (SkyscrapersElementView) dragStart, lastCellPressed);
                            if (editLine.canExecute()) {
                                editLine.execute();
                                getInstance().getHistory().pushChange(editLine);
                            } else {
                                treePanel.updateError(editLine.getError());
                            }
                        }
                    }*/
                }
            }
            dragStart = null;
            lastCellPressed = null;
        }
    }

    @Override
    public void changeCell(MouseEvent e, PuzzleElement element) {
    	SkyscrapersCell cell = (SkyscrapersCell) element;
        if (e.getButton() == MouseEvent.BUTTON1) {
        	if (cell.getData() < cell.getMax()) {
                cell.setData(cell.getData()+1);
            } else {
                cell.setData(0);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
        	if (cell.getData() > 0) {
                cell.setData(cell.getData()-1);
            } else {
                cell.setData(cell.getMax());
            }
        }
    }
}
