package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FillapixCellController extends ElementController
{
    @Override
    public void changeCell(MouseEvent e, PuzzleElement data)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(e.isControlDown())
            {
                this.boardView.getSelectionPopupMenu().show(boardView, this.boardView.getCanvas().getX() + e.getX(), this.boardView.getCanvas().getY() + e.getY());
            }
            else
            {
                Point location = new Point(data.getIndex() / boardView.getWidth(), data.getIndex() % boardView.getWidth());
                FillapixCell cell = new FillapixCell((Integer) data.getData(), location);
                if(cell.isUnknown())
                {
                    data.setData(cell.getData() + FillapixCell.BLACK);
                }
                else if(cell.isBlack())
                {
                    data.setData(cell.getData() + FillapixCell.WHITE);
                }
                else if(cell.isWhite())
                {
                    data.setData(cell.getData() + FillapixCell.UNKNOWN);
                }
            }
        }
    }
}