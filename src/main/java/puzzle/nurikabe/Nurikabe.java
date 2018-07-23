package puzzle.nurikabe;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.Element;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.rules.*;
import ui.boardview.ElementView;

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
        NurikabeBoard board= (NurikabeBoard)currentBoard;
        boardView = new NurikabeView(board.getDimension());
        addBoardListener(boardView);

        for(ElementView elementView: boardView.getElementViews())
        {
            NurikabeCell cell = (NurikabeCell)currentBoard.getElementData(elementView.getElement());

            cell.setIndex(cell.getIndex());
            elementView.setElement(cell);
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
