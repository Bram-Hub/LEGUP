package edu.rpi.legup.puzzle.masyu;

import static edu.rpi.legup.app.GameBoardFacade.getInstance;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.BoardView;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MasyuController extends ElementController {

    private MasyuElementView mousePressedCell;
    private MasyuElementView mouseDraggedCell;
    private List<MasyuElementView> masyuLine;

    public MasyuController() {
        super();
        this.mousePressedCell = null;
        this.mouseDraggedCell = null;
        this.masyuLine = new ArrayList<>();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        this.masyuLine.clear();
        mousePressedCell = (MasyuElementView) boardView.getElement(e.getPoint());
        masyuLine.add(mousePressedCell);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        MasyuElementView elementView = (MasyuElementView) boardView.getElement(e.getPoint());
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        if (mousePressedCell != null && elementView != null) {
            if (mouseDraggedCell == null) {
                mouseDraggedCell = elementView;
                Point p1 = mousePressedCell.getPuzzleElement().getLocation();
                Point p2 = mouseDraggedCell.getPuzzleElement().getLocation();

                if (Math.abs(p1.x - p2.x) == 1 ^ Math.abs(p1.y - p2.y) == 1) {
                    masyuLine.add(elementView);
                    MasyuLine newLine =
                            new MasyuLine(
                                    mousePressedCell.getPuzzleElement(),
                                    mouseDraggedCell.getPuzzleElement());
                    puzzle.notifyBoardListeners(listener -> listener.onBoardDataChanged(newLine));
                }
            } else {
                if (mouseDraggedCell != elementView) {
                    Point p1 = mouseDraggedCell.getPuzzleElement().getLocation();
                    Point p2 = elementView.getPuzzleElement().getLocation();

                    if (Math.abs(p1.x - p2.x) == 1 ^ Math.abs(p1.y - p2.y) == 1) {
                        masyuLine.add(elementView);
                        MasyuLine newLine =
                                new MasyuLine(
                                        mouseDraggedCell.getPuzzleElement(),
                                        elementView.getPuzzleElement());
                        puzzle.notifyBoardListeners(
                                listener -> listener.onBoardDataChanged(newLine));
                    }
                    mouseDraggedCell = elementView;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        mousePressedCell = null;
        mouseDraggedCell = null;
        masyuLine.clear();
    }

    /**
     * Alters the cells as they are being dragged over or clicked
     *
     * @param e Mouse event being used
     * @param data Data of selected cell
     */
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data) {
        MasyuCell cell = (MasyuCell) data;
        if (cell.getData() == MasyuType.BLACK || cell.getData() == MasyuType.WHITE) {
            return;
        }
        if (cell.getData() == MasyuType.UNKNOWN) {
            data.setData(3);
        } else {
            data.setData(0);
        }
    }
}
