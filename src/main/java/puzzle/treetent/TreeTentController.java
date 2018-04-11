package puzzle.treetent;

import app.GameBoardFacade;
import controller.ElementController;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import ui.boardview.BoardView;
import ui.treeview.TreeView;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class TreeTentController extends ElementController
{

    private TreeTentElement lastCellPressed;

    public TreeTentController()
    {
        super();
        this.lastCellPressed = null;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        Board board = getInstance().getBoard();
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        lastCellPressed = (TreeTentElement) boardView.getElement(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();
        Tree tree = getInstance().getTree();
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        TreeTentElement element = (TreeTentElement) boardView.getElement(e.getPoint());
        if(lastCellPressed != null && element != null)
        {
            TreeTentLine line = new TreeTentLine((TreeTentCell) lastCellPressed.getData(), (TreeTentCell) element.getData());
            board.getLines().add(line);
            board.getModifiedData().add(line);
            boardView.updateBoard(board);
        }
        lastCellPressed = element;
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
