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
}
