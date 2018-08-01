package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.ui.treeview.TreeView;

import java.awt.event.MouseEvent;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

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
//        TreeTentElementView puzzleElement = (TreeTentElementView) boardView.getPuzzleElement(e.getPoint());
//        if(lastCellPressed != null && puzzleElement != null)
//        {
//            TreeTentLine line = new MasyuLine((TreeTentCell) lastCellPressed.getPuzzleElement(), (TreeTentCell) puzzleElement.getPuzzleElement());
//            board.getLines().add(line);
//            board.getModifiedData().add(line);
//            boardView.updateBoard(board);
//        }
//        lastCellPressed = puzzleElement;
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
            TreeTentLine line = new TreeTentLine((TreeTentCell) lastCellPressed.getPuzzleElement(), (TreeTentCell) element.getPuzzleElement());
            board.getLines().add(line);
            board.getModifiedData().add(line);
            boardView.onBoardChanged(board);
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
