package edu.rpi.legup.puzzle.treetent;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.puzzle.treetent.rules.*;

public class TreeTent extends Puzzle
{

    public TreeTent()
    {
        super();

        this.name = "TreeTent";

        this.importer = new TreeTentImporter(this);
        this.exporter = new TreeTentExporter(this);

        this.factory = new TreeTentCellFactory();

        this.basicRules.add(new EmptyFieldBasicRule());
        this.basicRules.add(new FinishWithGrassBasicRule());
        this.basicRules.add(new FinishWithTentsBasicRule());
        this.basicRules.add(new LastCampingSpotBasicRule());
        this.basicRules.add(new TentForTreeBasicRule());
        this.basicRules.add(new TreeForTentBasicRule());

        this.contradictionRules.add(new NoTentForTreeContradictionRule());
        this.contradictionRules.add(new NoTreeForTentContradictionRule());
        this.contradictionRules.add(new TooFewTentsContradictionRule());
        this.contradictionRules.add(new TooManyTentsContradictionRule());
        this.contradictionRules.add(new TouchingTentsContradictionRule());

        this.caseRules.add(new FillinRowCaseRule());
        this.caseRules.add(new LinkTentCaseRule());
        this.caseRules.add(new LinkTreeCaseRule());
        this.caseRules.add(new TentOrGrassCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        TreeTentBoard board = (TreeTentBoard)currentBoard;
        boardView = new TreeTentView((TreeTentBoard)currentBoard);
    }

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     *
     * @return board of the random edu.rpi.legup.puzzle
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
        return false;
    }

    /**
     * Callback for when the board element changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board)
    {

    }
}
