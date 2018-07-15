package puzzle.sudoku;

import model.gameboard.Board;
import model.Puzzle;
import model.gameboard.ElementData;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.sudoku.rules.*;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;

public class Sudoku extends Puzzle
{
    private SudokuView boardView;

    /**
     * Sudoku Constructor
     */
    public Sudoku()
    {
        super();

        this.name = "Sudoku";

        this.importer = new SudokuImporter(this);
        this.exporter = new SudokuExporter(this);

        this.factory = new SudokuCellFactory();

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
    public void initializeView()
    {
        SudokuBoard board= (SudokuBoard)currentBoard;
        boardView = new SudokuView(board.getDimension());
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            SudokuCell cell = (SudokuCell) currentBoard.getElementData(index);

            cell.setIndex(index);
            element.setData(cell);
        }
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
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     *
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board)
    {
        SudokuBoard sudokuBoard = (SudokuBoard) board;
        TreeTransition transition = new TreeTransition(null, sudokuBoard);

        for(ContradictionRule rule : contradictionRules)
        {
            if(rule.checkContradiction(transition) == null)
            {
                return false;
            }
        }

        for(ElementData data : sudokuBoard.getElementData())
        {
            if(data.getValueInt() == 0)
            {
                return false;
            }
        }
        return true;
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
}
