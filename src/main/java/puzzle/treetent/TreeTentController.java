package puzzle.treetent;

import app.GameBoardFacade;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import java.awt.Point;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;
import ui.treeview.TreeElementView;
import ui.treeview.TreeSelection;
import ui.treeview.TreeView;
import utility.EditDataCommand;
import utility.ICommand;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class TreeTentController extends ElementController
{

    private TreeTentElement lastCellPressed;
    private TreeTentElement dragStart;
    public TreeTentController()
    {
        super();
        this.dragStart = null;
        this.lastCellPressed = null;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement data = boardView.getElement(e.getPoint());
        if(data instanceof TreeTentElement)
        {
            dragStart = (TreeTentElement) data;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
//        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();
//        Tree tree = getInstance().getTree();
//        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
//        BoardView boardView = getInstance().getLegupUI().getBoardView();
//        TreeTentElement element = (TreeTentElement) boardView.getElement(e.getPoint());
//        if(lastCellPressed != null && element != null)
//        {
//            TreeTentLine line = new TreeTentLine((TreeTentCell) lastCellPressed.getData(), (TreeTentCell) element.getData());
//            board.getLines().add(line);
//            board.getModifiedData().add(line);
//            boardView.updateBoard(board);
//        }
//        lastCellPressed = element;
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        PuzzleElement elementView = boardView.getElement(e.getPoint());
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();

        TreeTentElement dragEnd = null;
        TreeTentClueView clueView = null;
        if(elementView instanceof TreeTentElement) {
            dragEnd = (TreeTentElement) elementView;
        }
        else {
            clueView = (TreeTentClueView)elementView;
        }

        if(dragStart != null && dragEnd != null) {
            if (dragStart.getLocation().distance(dragEnd.getLocation()) / 30 == 1) {
                TreeTentLine line = new TreeTentLine((TreeTentCell) dragStart.getData(), (TreeTentCell) dragEnd.getData());
                ICommand edit = new EditLineCommand(elementView, selectedView, e, line);
                getInstance().getHistory().pushChange(edit);
                edit.execute();
            } else {
                if (!((TreeTentCell) dragEnd.getData()).isLinked())
                    super.mouseReleased(e);
            }
        }
        else
        {
            super.mouseReleased(e);
        }
    }
    @Override
    public void changeCell(MouseEvent e, ElementData data)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(e.isControlDown())
            {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else
            {
                if(data.getValueInt() == 0)
                {
                    data.setValueInt(2);
                }
                else if(data.getValueInt() == 2)
                {
                    data.setValueInt(3);
                }
                else
                {
                    data.setValueInt(0);
                }
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(data.getValueInt() == 0)
            {
                data.setValueInt(3);
            }
            else if(data.getValueInt() == 2)
            {
                data.setValueInt(0);
            }
            else
            {
                data.setValueInt(2);
            }
        }
    }
}
