package puzzles.sudoku;

import app.BoardController;
import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.GridBoard;
import ui.boardview.GridBoardView;
import ui.boardview.GridElement;
import ui.boardview.PuzzleElement;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SudokuView extends GridBoardView
{
    private SudokuCellController sudokuCellController;

    public SudokuView(BoardController boardController, Dimension gridSize, Dimension elementSize)
    {
        super(boardController, gridSize, elementSize);
        sudokuCellController = new SudokuCellController(this);
        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                SudokuElement element = new SudokuElement(new SudokuCell(0, new Point(i, k)));
                element.addMouseListener(new SudokuCellController(this));
                element.setSize(elementSize);
                element.addMouseListener(sudokuCellController);
                puzzleElements.add(element);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(3));
        graphics2D.drawRect(0,0,
                gridSize.width * elementSize.width,
                gridSize.height * elementSize.height);

        int regions = (int)Math.sqrt(gridSize.width);
        for(int i = 0; i < regions; i++)
        {
            for(int k = 0; k < regions; k++)
            {
                graphics2D.drawRect(i * regions * elementSize.width, k * regions * elementSize.height,
                        regions * elementSize.width, regions * elementSize.height);
            }
        }

        graphics2D.setStroke(new BasicStroke(1));
        for(PuzzleElement element: puzzleElements)
        {
            element.draw(graphics2D);
        }
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
        revalidate();
        repaint();
    }
}
