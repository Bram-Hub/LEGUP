package puzzle.fillapix;

import model.Puzzle;
import model.gameboard.Board;
import puzzle.fillapix.rules.*;
import ui.boardview.BoardView;
import ui.boardview.ElementView;

import java.awt.*;

public class Fillapix extends Puzzle
{
    private FillapixView boardView;

    /**
     * Fillapix Constructor
     */
    public Fillapix()
    {
        super();

        this.name = "Fillapix";

        this.importer = new FillapixImporter(this);
        this.exporter = new FillapixExporter(this);

        this.factory = new FillapixCellFactory();

        //basicRules.add(new AdvancedDeductionBasicRule());
        basicRules.add(new FinishWithBlackBasicRule());
        basicRules.add(new FinishWithWhiteBasicRule());

        caseRules.add(new BlackOrWhiteCaseRule());

        contradictionRules.add(new TooFewBlackCellsContradictionRule());
        contradictionRules.add(new TooManyBlackCellsContradictionRule());
    }

    /**
     * Initializes the game board
     */
    @Override
    public void initializeView()
    {
        FillapixBoard board = (FillapixBoard) currentBoard;
        boardView = new FillapixView(new Dimension(board.getWidth(), board.getHeight()));
        for(ElementView element : boardView.getElementViews())
        {
            FillapixCell cell = (FillapixCell) currentBoard.getElementData(element.getElement());

            cell.setIndex(cell.getIndex());
            element.setElement(cell);
        }
    }

    @Override
    public Board generatePuzzle(int difficulty)
    {
        return null;
    }

    @Override
    public boolean isPuzzleComplete()
    {
        return false;
    }

    @Override
    public boolean isBoardComplete(Board board)
    {
        return false;
    }

    @Override
    public void onBoardChange(Board board)
    {

    }

    public BoardView getBoardView()
    {
        return boardView;
    }
}