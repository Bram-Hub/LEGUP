package puzzle.fillapix;

import controller.ElementController;
import model.gameboard.ElementData;

import java.awt.*;
import java.awt.event.MouseEvent;

public class FillapixCellController extends ElementController
{
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
                Point location = new Point(data.getIndex() / boardView.getWidth(), data.getIndex() % boardView.getWidth());
                FillapixCell cell = new FillapixCell(data.getValueInt(), location);
                if(cell.isUnknown())
                {
                    data.setValueInt(cell.getValueInt() + FillapixCell.BLACK);
                }
                else if(cell.isBlack())
                {
                    data.setValueInt(cell.getValueInt() + FillapixCell.WHITE);
                }
                else if(cell.isWhite())
                {
                    data.setValueInt(cell.getValueInt() + FillapixCell.UNKNOWN);
                }
            }
        }
    }
}