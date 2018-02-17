package puzzles.sudoku;

import app.BoardController;
import ui.boardview.GridBoardView;

import java.awt.*;

public class SudokuView extends GridBoardView
{
    public SudokuView(BoardController boardController, Dimension gridDimension, Dimension elementDimension)
    {
        super(boardController, gridDimension, elementDimension);
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.drawString("Hello", 50, 50);
    }
}
