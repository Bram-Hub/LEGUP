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
        dragStart = (TreeTentElement) boardView.getElement(e.getPoint());
        lastCellPressed = (TreeTentElement) boardView.getElement(e.getPoint());
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
        TreeTentElement dragEnd = (TreeTentElement) boardView.getElement(e.getPoint());
        TreeView treeView = GameBoardFacade.getInstance().getLegupUI().getTreePanel().getTreeView();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        PuzzleElement elementView = boardView.getElement(e.getPoint());
        TreeSelection selection = treeView.getTreeSelection();
        TreeElementView selectedView = selection.getFirstSelection();
        TreeTentBoard board = (TreeTentBoard)getInstance().getBoard();
        if(dragStart.getLocation().distance(dragEnd.getLocation())/30 == 1){
            TreeTentLine line = new TreeTentLine((TreeTentCell) dragStart.getData(), (TreeTentCell) dragEnd.getData());
            boolean mod_contains = false;
            boolean contains = false;
            ElementData dup_line = null;
            ICommand edit = new EditLineCommand(elementView, selectedView, e, line);
            getInstance().getHistory().pushChange(edit);
            edit.execute();
//            for(int i = 0;i < board.getModifiedData().size();i++){
//                if(board.getModifiedData().get(i).getValueString() == "LINE"){
//                    if(line.compare((TreeTentLine) board.getModifiedData().get(i))){
//                        System.out.println("contains");
//                        dup_line = board.getModifiedData().get(i);
//                        mod_contains = true;
//                    }
//                }
//            }
//            for(int i = 0;i < board.getLines().size();i++){
//                if(board.getLines().get(i).compare(line)){
//                    contains = true;
//                }
//            }
//            if(contains || mod_contains){
//                if(mod_contains){
//
//                    ICommand edit = new EditDataCommand(elementView, selectedView, e);
//                    getInstance().getHistory().pushChange(edit);
//                    edit.execute();
//                    board.getModifiedData().remove(dup_line);
//                    board.getLines().remove(dup_line);
//                    boardView.updateBoard(board);
//                }
//            } else {
//
//                ICommand edit = new EditDataCommand(elementView, selectedView, e);
//                getInstance().getHistory().pushChange(edit);
//                edit.execute();
//                board = (TreeTentBoard) selection.getFirstSelection().getTreeElement().getBoard();
//                board.getModifiedData().add(line);
//                board.getLines().add(line);
//                boardView.updateBoard(board);
//            }
        }
        else{
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
