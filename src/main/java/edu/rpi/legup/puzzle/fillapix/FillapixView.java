package edu.rpi.legup.puzzle.fillapix;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.ui.boardview.GridBoardView;

import java.awt.*;

public class FillapixView extends GridBoardView
{
    public FillapixView(FillapixBoard board)
    {
        super(new BoardController(), new FillapixCellController(), board.getDimension());

        for(Element element : board.getElementData())
        {
            FillapixCell cell = (FillapixCell)element;
            Point loc = cell.getLocation();
            FillapixElementView elementView = new FillapixElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }
}