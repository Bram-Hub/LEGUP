package puzzle.treetent;

import app.GameBoardFacade;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.Element;
import model.tree.Tree;
import ui.boardview.BoardView;
import ui.treeview.TreeView;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class TreeTentController extends ElementController
{

    private TreeTentElementView lastCellPressed;
    private TreeTentElementView dragStart;
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
        dragStart = (TreeTentElementView) boardView.getElement(e.getPoint());
        lastCellPressed = (TreeTentElementView) boardView.getElement(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
//        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();
//        Tree tree = getInstance().getTree();
//        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
//        BoardView boardView = getInstance().getLegupUI().getBoardView();
//        TreeTentElementView element = (TreeTentElementView) boardView.getElement(e.getPoint());
//        if(lastCellPressed != null && element != null)
//        {
//            TreeTentLine line = new TreeTentLine((TreeTentCell) lastCellPressed.getElement(), (TreeTentCell) element.getElement());
//            board.getLines().add(line);
//            board.getModifiedData().add(line);
//            boardView.updateBoard(board);
//        }
//        lastCellPressed = element;
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        TreeTentElementView dragEnd = (TreeTentElementView) boardView.getElement(e.getPoint());
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();
        TreeTentElementView element = (TreeTentElementView) boardView.getElement(e.getPoint());
        if(lastCellPressed != null && element != null)
        {
            TreeTentLine line = new TreeTentLine((TreeTentCell) lastCellPressed.getElement(), (TreeTentCell) element.getElement());
            board.getLines().add(line);
            board.getModifiedData().add(line);
            boardView.onBoardChanged(board);
        }
    }
    @Override
    public void changeCell(MouseEvent e, Element data)
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
