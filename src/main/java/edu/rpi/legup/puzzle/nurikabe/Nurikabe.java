package edu.rpi.legup.puzzle.nurikabe;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.rules.*;

public class Nurikabe extends Puzzle
{
    public Nurikabe()
    {
        super();

        this.name = "Nurikabe";

        this.importer = new NurikabeImporter(this);
        this.exporter = new NurikabeExporter(this);

        this.factory = new NurikabeCellFactory();

        this.basicRules.add(new BlackBetweenRegionsBasicRule());
        this.basicRules.add(new BlackBottleNeckBasicRule());
        this.basicRules.add(new CornerBlackBasicRule());
        this.basicRules.add(new FillinBlackBasicRule());
        this.basicRules.add(new FillinWhiteBasicRule());
        this.basicRules.add(new PreventBlackSquareBasicRule());
        this.basicRules.add(new SurroundRegionBasicRule());
        this.basicRules.add(new WhiteBottleNeckBasicRule());
        this.basicRules.add(new UnreachableBasicRule());

        this.contradictionRules.add(new BlackSquareContradictionRule());
        this.contradictionRules.add(new IsolateBlackContradictionRule());
        this.contradictionRules.add(new MultipleNumbersContradictionRule());
        this.contradictionRules.add(new NoNumberContradictionRule());
        this.contradictionRules.add(new TooFewSpacesContradictionRule());
        this.contradictionRules.add(new TooManySpacesContradictionRule());
        this.contradictionRules.add(new CantReachWhiteContradictionRule());

        this.caseRules.add(new BlackOrWhiteCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        boardView = new NurikabeView((NurikabeBoard)currentBoard);
        addBoardListener(boardView);
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
        NurikabeBoard nurikabeBoard = (NurikabeBoard)board;
        TreeTransition transition = new TreeTransition(null, nurikabeBoard);


        for(ContradictionRule rule : contradictionRules)
        {
            if(rule.checkContradiction(transition) == null)
            {
                return false;
            }
        }
        for(Element data : nurikabeBoard.getElementData())
        {
            NurikabeCell cell = (NurikabeCell) data;
            if(cell.getType() == NurikabeType.UNKNOWN)
            {
                return false;
            }
        }
        return true;
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
