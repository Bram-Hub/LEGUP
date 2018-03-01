package puzzles.sudoku;

import app.BoardController;
import model.gameboard.Board;
import model.Puzzle;
import model.gameboard.ElementData;
import model.gameboard.GridCell;
import model.rules.Tree;
import ui.Selection;
import ui.boardview.BoardView;
import ui.boardview.GridElement;
import ui.boardview.PuzzleElement;

import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

public class Sudoku extends Puzzle
{
    private SudokuView boardView;
    private int[][] testBoard = new int[][]{{0, 0, 0, 0, 0, 3, 0, 2, 0},
                                            {1, 4, 0, 6, 2, 8, 0, 0, 0},
                                            {6, 8, 2, 0, 0, 0, 3, 4, 0},
                                            {5, 9, 0, 8, 0, 0, 0, 1, 0},
                                            {0, 0, 0, 7, 9, 1, 0, 0, 0},
                                            {0, 1, 0, 0, 0, 2, 0, 3, 8},
                                            {0, 5, 1, 0, 0, 0, 6, 7, 2},
                                            {0, 0, 0, 2, 7, 5, 0, 8, 9},
                                            {0, 2, 0, 1, 0, 0, 0, 0, 0}};

    /**
     * Sudoku Constructor
     */
    public Sudoku()
    {
        super();
        boardView = new SudokuView(new BoardController(),
                new Dimension(9, 9),
                new Dimension(36,36));
        initializeBoard();

        basicRules.add(new AdvancedDeductionBasicRule());
        basicRules.add(new LastCellForNumberBasicRule());
        basicRules.add(new LastNumberForCellBasicRule());

        caseRules.add(new PossibleCellCaseRule());
        caseRules.add(new PossibleNumberCaseRule());

        contradictionRules.add(new NoSolutionContradictionRule());
        contradictionRules.add(new RepeatedNumberContradictionRule());
    }

    public BoardView getBoardView()
    {
        return boardView;
    }

    /**
     * Initializes the game board
     */
    @Override
    public void initializeBoard()
    {
        currentBoard = new SudokuBoard(9, 9);
        tree = new Tree(currentBoard);
        int width = ((SudokuBoard)currentBoard).getWidth();
        int height = ((SudokuBoard)currentBoard).getHeight();
        ArrayList<ElementData> data = new ArrayList<>();
        Random rand = new Random();
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            SudokuCell cell = new SudokuCell(testBoard[index / width][index % width], new Point(index % width, index / height));
            if(cell.getValueInt() > 0)
            {
                cell.setModifiable(false);
                cell.setGiven(true);
            }
            else
                cell.setModifiable(true);
            cell.setIndex(index);
            element.setData(cell);
            data.add(cell);
        }
        currentBoard.setElementData(data);
    }

    /**
     * Generates a random puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     *
     * @return board of the random puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty)
    {
        return null;
    }

    /**
     * Determines if the puzzle was solves correctly
     *
     * @return true if the board was solved correctly, false otherwise
     */
    @Override
    public boolean isPuzzleComplete()
    {
        return false;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     *
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isValidBoardState(Board board)
    {
        return false;
    }

    /**
     * Callback for when the board data changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board)
    {

    }

    /**
     * Callback for when the tree selection changes
     *
     * @param newSelection
     */
    @Override
    public void onTreeSelectionChange(ArrayList<Selection> newSelection)
    {

    }

    /**
     * Imports the board using the file stream
     *
     * @param fileStream
     *
     * @return
     */
    @Override
    public Board importPuzzle(FileInputStream fileStream)
    {
        return null;
    }
}
