package puzzle.fillapix;

import controller.BoardController;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.DataSelectionView;
import ui.boardview.GridBoardView;
import ui.boardview.PuzzleElement;
import ui.boardview.SelectionItemView;

import javax.swing.*;
import java.awt.*;

public class FillapixView extends GridBoardView
{
    private static final Color STROKE_COLOR = new Color(0,0,0);
    private static final Stroke MINOR_STOKE = new BasicStroke(1);

    public FillapixView(Dimension gridSize)
    {
        super(new BoardController(), new FillapixCellController(), gridSize);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);

        for(int i = 0; i < gridSize.height; i++)
        {
            for(int j = 0; j < gridSize.width; j++)
            {
                Point location = new Point(j * elementSize.width, i * elementSize.height);
                FillapixElement element = new FillapixElement(new FillapixCell(-1, null));
                element.setIndex(i * gridSize.width + j);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MINOR_STOKE);
        PuzzleElement hover = null;
        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                PuzzleElement element = puzzleElements.get(i * gridSize.height + k);
                if(!element.isHover())
                    element.draw(graphics2D);
                else
                    hover = element;
            }
        }

        if(hover != null)
            hover.draw(graphics2D);
    }

    protected Dimension getProperSize()
    {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = gridSize.width * (elementSize.width + 1) + 9;
        boardViewSize.height = gridSize.height * (elementSize.height + 1) + 9;
        return boardViewSize;
    }

    /**
     * Board Data changed
     *
     * @param board board to update the BoardView
     */
    public void updateBoard(Board board)
    {
        GridBoard gridBoard = (GridBoard)board;
        for(PuzzleElement element: puzzleElements)
        {
            element.setData(gridBoard.getElementData(element.getIndex()));
        }
        repaint();
    }

    public DataSelectionView getSelectionPopupMenu()
    {
        DataSelectionView selectionView = new DataSelectionView(elementController);
        GridLayout layout = new GridLayout(3,3);
        selectionView.setLayout(layout);
        for (int r = 1; r <= 3; r++)
        {
            for (int c = 1; c <= 3; c++)
            {
                SelectionItemView item = new SelectionItemView(new FillapixCell((r - 1) * 3 + c, null));
                item.addActionListener(elementController);
                item.setHorizontalTextPosition(SwingConstants.CENTER);
                selectionView.add(item);
            }
        }
        return selectionView;
    }
}