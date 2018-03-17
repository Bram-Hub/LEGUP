package puzzle.lightup;

import controller.ElementController;
import model.gameboard.ElementData;

import java.awt.event.MouseEvent;

public class LightUpCellController extends ElementController
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
                if(data.getValueInt() == -2)
                {
                    data.setValueInt(-4);
                }
                else if(data.getValueInt() == -4)
                {
                    data.setValueInt(-3);
                }
                else
                {
                    data.setValueInt(-2);
                }
            }
        }
        else if(e.getButton() == MouseEvent.BUTTON3)
        {
            if(data.getValueInt() == -4)
            {
                data.setValueInt(-2);
            }
            else if(data.getValueInt() == -2)
            {
                data.setValueInt(-3);
            }
            else
            {
                data.setValueInt(-4);
            }
        }
    }
}
