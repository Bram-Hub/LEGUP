package puzzle.fillapix;

import controller.BoardController;
import model.gameboard.Board;
import ui.boardview.ElementView;
import ui.boardview.GridBoardView;

import java.awt.*;

public class FillapixView extends GridBoardView
{
    public FillapixView(Dimension gridSize)
    {
        super(new BoardController(), new FillapixCellController(), gridSize);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int j = 0; j < gridSize.width; j++)
            {
                Point location = new Point(j * elementSize.width, i * elementSize.height);
                FillapixElementView element = new FillapixElementView(new FillapixCell(-2, null));
                element.setIndex(i * gridSize.width + j);
                element.setSize(elementSize);
                element.setLocation(location);
                elementViews.add(element);
            }
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
            element.setElement(fillapixBoard.getElementData(element.getIndex()));
        }
        repaint();
    }
}