package puzzles.sudoku;

import app.BoardController;
import app.ElementController;
import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.GridBoardView;
import ui.boardview.GridElement;
import ui.boardview.PuzzleElement;

import java.awt.*;

public class SudokuView extends GridBoardView
{
    private SudokuCellController sudokuCellController;

    public SudokuView(BoardController boardController, Dimension gridSize, Dimension elementSize)
    {
        super(boardController, gridSize, elementSize);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);
        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                Point location = new Point(k * elementSize.width, i * elementSize.height);
                SudokuElement element = new SudokuElement(new SudokuCell(0, location));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setStroke(new BasicStroke(1));
        PuzzleElement hover = null;
        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                PuzzleElement element = puzzleElements.get(i * gridSize.height + k);
                element.setLocation(new Point(k * elementSize.width, i * elementSize.height));
                element.setSize(elementSize);
                if(!element.isHover())
                    element.draw(graphics2D);
                else
                    hover = element;
            }
        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(0,0,
                gridSize.width * elementSize.width,
                gridSize.height * elementSize.height);

        int regions = (int)Math.sqrt(gridSize.width);
        elementSize = new Dimension(canvas.getWidth() / gridSize.width, canvas.getHeight() / gridSize.height);
        canvas.setSize(elementSize.width * gridSize.width, elementSize.height * gridSize.height);
        for(int i = 0; i < regions; i++)
        {
            for(int k = 0; k < regions; k++)
            {
                graphics2D.drawRect(i * regions * elementSize.width, k * regions * elementSize.height,
                        regions * elementSize.width, regions * elementSize.height);
            }
        }

        if(hover != null)
            hover.draw(graphics2D);

        //System.out.println("Canvas: " + canvas.getBounds());
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
}
