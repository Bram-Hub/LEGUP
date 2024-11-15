package edu.rpi.legup.puzzle.kakurasu;

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
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeViewSelection;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class KakurasuController extends ElementController {

    private ElementView lastCellPressed;

    public KakurasuController() {
        super();
        this.lastCellPressed = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON2) {
            BoardView boardView = getInstance().getLegupUI().getBoardView();
            if (boardView != null) {
                lastCellPressed = boardView.getElement(e.getPoint());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO: Figure out if mouseReleased is still needed, and what it needs to do
        if (GameBoardFacade.getInstance().getLegupUI().getTreePanel() != null
                && e.getButton() != MouseEvent.BUTTON2) {
            lastCellPressed = null;
        } else {
            super.mouseReleased(e);
        }
    }

    @Override
    public void changeCell(MouseEvent e, PuzzleElement element) {
        KakurasuCell cell = (KakurasuCell) element;
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (cell.getData() == KakurasuType.UNKNOWN) {
                element.setData(KakurasuType.FILLED);
            } else {
                if (cell.getData() == KakurasuType.FILLED) {
                    element.setData(KakurasuType.EMPTY);
                } else {
                    element.setData(KakurasuType.UNKNOWN);
                }
            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (cell.getData() == KakurasuType.UNKNOWN) {
                    element.setData(KakurasuType.EMPTY);
                } else {
                    if (cell.getData() == KakurasuType.EMPTY) {
                        element.setData(KakurasuType.FILLED);
                    } else {
                        element.setData(KakurasuType.UNKNOWN);
                    }
                }
            }
        }
    }
}
