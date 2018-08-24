package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.history.EditDataCommand;
import edu.rpi.legup.history.ICommand;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.treeview.TreePanel;
import edu.rpi.legup.ui.treeview.TreeView;
import edu.rpi.legup.ui.treeview.TreeViewSelection;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

public class TreeTentController extends ElementController
{

    private ElementView lastCellPressed;
    private ElementView dragStart;

    public TreeTentController()
    {
        super();
        this.dragStart = null;
        this.lastCellPressed = null;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON3) {
            BoardView boardView = getInstance().getLegupUI().getBoardView();
            dragStart = boardView.getElement(e.getPoint());
            lastCellPressed = boardView.getElement(e.getPoint());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() != MouseEvent.BUTTON3) {
            TreePanel treePanel = GameBoardFacade.getInstance().getLegupUI().getTreePanel();
            TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
            BoardView boardView = getInstance().getLegupUI().getBoardView();
            lastCellPressed = boardView.getElement(e.getPoint());
            TreeViewSelection selection = treeView.getSelection();
            if (dragStart != null && dragStart == lastCellPressed) {
                if(dragStart.getPuzzleElement().getIndex() >= 0) {
                    ICommand edit = new EditDataCommand(lastCellPressed, selection, e);
                    if (edit.canExecute()) {
                        edit.execute();
                        getInstance().getHistory().pushChange(edit);
                        treePanel.updateError("");
                    } else {
                        treePanel.updateError(edit.getError());
                    }
                } else {
                    ClueCommand edit = new ClueCommand(selection, (TreeTentClueView)dragStart);
                    if (edit.canExecute()) {
                        edit.execute();
                        getInstance().getHistory().pushChange(edit);
                        treePanel.updateError("");
                    } else {
                        treePanel.updateError(edit.getError());
                    }
                }
            } else if (dragStart != null && lastCellPressed != null) {
                ICommand editLine = new EditLineCommand(selection, (TreeTentElementView) dragStart, (TreeTentElementView) lastCellPressed);
                if (editLine.canExecute()) {
                    editLine.execute();
                    getInstance().getHistory().pushChange(editLine);
                } else {
                    treePanel.updateError(editLine.getError());
                }
            }
            dragStart = null;
            lastCellPressed = null;
        }
    }

    @Override
    public void changeCell(MouseEvent e, PuzzleElement data)
    {
        TreeTentCell cell = (TreeTentCell)data;
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(e.isControlDown())
            {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else
            {
                if(cell.getData() == 0)
                {
                    data.setData(2);
                }
                else if(cell.getData() == 2)
                {
                    data.setData(3);
                }
                else
                {
                    data.setData(0);
                }
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(cell.getData() == 0)
            {
                data.setData(3);
            }
            else if(cell.getData() == 2)
            {
                data.setData(0);
            }
            else
            {
                data.setData(2);
            }
        }
    }
}
