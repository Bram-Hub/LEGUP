package puzzle.fillapix;

import controller.BoardController;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.GridBoardView;
import ui.boardview.PuzzleElement;

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
                FillapixElement element = new FillapixElement(new FillapixCell(-2, null));
                element.setIndex(i * gridSize.width + j);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }

    /**
     * Board Data changed
     *
     * @param board board to update the BoardView
     */
    public void updateBoard(Board board)
    {
        FillapixBoard fillapixBoard = (FillapixBoard) board;
        for(PuzzleElement element : puzzleElements)
        {
            element.setData(fillapixBoard.getElementData(element.getIndex()));
        }
        repaint();
    }
}