package app;

import model.Puzzle;
import model.gameboard.Board;
import ui.LegupUI;
import ui.Selection;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;

import java.awt.*;
import java.awt.event.*;

public class BoardController extends Controller
{
    protected Point lastLeftMousePoint;
    protected Point lastRightMousePoint;

    /**
     * BoardController Constructor - creates a controller object to listen
     * to ui events from a DynamicViewer
     */
    public BoardController()
    {
        super();
        lastLeftMousePoint = null;
        lastRightMousePoint = null;
    }

    /**
     * Mouse Clicked event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /**
     * Mouse Pressed event - sets the cursor to the move cursor and stores
     * info for possible panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
//        Point point = viewer.toDrawCoordinates(e.getPoint());
//
//        GameBoardFacade facade = GameBoardFacade.getInstance();
//        LegupUI legupUI = facade.getLegupUI();
//        Selection selection = facade.getSelections().get(0);
//        Puzzle puzzle = facade.getPuzzleModule();
//
//        if(selection.isTransition())
//        {
//            legupUI.showStatus("You can not modify transitions", true);
//            return;
//        }
//        else if(puzzle == null)
//        {
//            return;
//        }
//
//        Board board = selection.getState();
//        BoardView boardView = null;
//        PuzzleElement puzzleElement = boardView.getElement(point);
//        int elementIndex = puzzleElement.getIndex();
//
//        switch(e.getButton())
//        {
//            case MouseEvent.BUTTON1:
//                Dimension elementSize = null;
//
//                lastLeftMousePoint = null;
//
//                if(false)
//                {
//
//                }
//                else if(false)
//                {
//
//                }
//                else
//                {
//                    lastLeftMousePoint = point;
//                    if(board.getElementData(elementIndex).isModifiable())
//                    {
//
//                    }
//                    else
//                    {
//                        legupUI.showStatus("You are not allowed to change that cell.", true);
//                    }
//                }
//
//
//                break;
//            case MouseEvent.BUTTON2:
//                break;
//            case MouseEvent.BUTTON3:
//                break;
//            default:
//                return;
//        }
    }

    /**
     * Mouse Released event - sets the cursor back to the default cursor and reset
     * info for panning
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
    }

    /**
     * Mouse Entered event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    /**
     * Mouse Exited event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    /**
     * Mouse Dragged event - adjusts the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
    }

    /**
     * Mouse Moved event - no default action
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

    /**
     * Mouse Wheel Moved event - zooms in on the viewport
     *
     * @param e MouseEvent object
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        super.mouseWheelMoved(e);
    }
}
