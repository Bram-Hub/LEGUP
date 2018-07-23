package puzzle.fillapix;

import controller.BoardController;
import model.gameboard.Board;
import model.gameboard.Element;
import ui.boardview.ElementView;
import ui.boardview.GridBoardView;

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

    /**
     * Board Data changed
     *
     * @param board board to update the BoardView
     */
    public void onBoardChanged(Board board)
    {
        FillapixBoard fillapixBoard = (FillapixBoard) board;
        for(ElementView element : elementViews)
        {
            element.setElement(fillapixBoard.getElementData(element.getElement()));
        }
        repaint();
    }
}