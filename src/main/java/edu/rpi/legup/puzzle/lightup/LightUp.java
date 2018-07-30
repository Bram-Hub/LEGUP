package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.RegisterPuzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.rules.*;

@RegisterPuzzle
public class LightUp extends Puzzle
{

    public LightUp()
    {
        super();
        name = "LightUp";

        importer = new LightUpImporter(this);
        exporter = new LightUpExporter(this);

        factory = new LightUpCellFactory();

        basicRules.add(new BulbsOutsideDiagonalBasicRule());
        basicRules.add(new EmptyCellinLightBasicRule());
        basicRules.add(new EmptyCornersBasicRule());
        basicRules.add(new FinishWithBulbsBasicRule());
        basicRules.add(new FinishWithEmptyBasicRule());
        basicRules.add(new MustLightBasicRule());

        contradictionRules.add(new BulbsInPathContradictionRule());
        contradictionRules.add(new CannotLightACellContradictionRule());
        contradictionRules.add(new TooFewBulbsContradictionRule());
        contradictionRules.add(new TooManyBulbsContradictionRule());

        caseRules.add(new LightOrEmptyCaseRule());
        caseRules.add(new SatisfyNumberCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        boardView = new LightUpView((LightUpBoard) currentBoard);
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
        LightUpBoard lightUpBoard = (LightUpBoard)board;
        lightUpBoard.fillWithLight();
        TreeTransition transition = new TreeTransition(null, lightUpBoard);

        for(ContradictionRule rule : contradictionRules)
        {
            if(rule.checkContradiction(transition) == null)
            {
                return false;
            }
        }
        for(Element data : lightUpBoard.getElementData())
        {
            LightUpCell cell = (LightUpCell)data;
            if((cell.getType() == LightUpCellType.UNKNOWN || cell.getType() == LightUpCellType.EMPTY) && !cell.isLite())
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
