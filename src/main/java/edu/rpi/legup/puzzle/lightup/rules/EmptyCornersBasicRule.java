package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class EmptyCornersBasicRule extends BasicRule
{

    public EmptyCornersBasicRule()
    {
        super("Empty Corners",
                "Cells on the corners of a number must be empty if placing bulbs would prevent the number from being satisfied.",
                "edu/rpi/legup/images/lightup/rules/EmptyCorners.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement index of the puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        LightUpBoard initialBoard = (LightUpBoard)transition.getParents().get(0).getBoard();
        LightUpCell cell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell finalCell = (LightUpCell) finalBoard.getPuzzleElement(puzzleElement);

        if(!(cell.getType() == LightUpCellType.UNKNOWN && finalCell.getType() == LightUpCellType.EMPTY))
        {
            return "Must be an empty cell";
        }

        TooFewBulbsContradictionRule tooFew = new TooFewBulbsContradictionRule();
//        for() {
//
//        }

        return null;
    }

    private boolean isForcedEmpty(LightUpBoard board, LightUpCell cell, Point loc)
    {
        if(cell == null || cell.getType() != LightUpCellType.NUMBER)
        {
            return false;
        }

        Point cellLoc = cell.getLocation();

        if(cellLoc.x == loc.x - 1)
        {
            LightUpCell checkCell = null;
        }
        else
        {

        }

        int bulbs = 0;
        int bulbsNeeded = cell.getData();
        cell = board.getCell(cellLoc.x + 1, cellLoc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x, cellLoc.y + 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x - 1, cellLoc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(cellLoc.x, cellLoc.y - 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        return bulbs == bulbsNeeded;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard lightUpBoard = (LightUpBoard)node.getBoard().copy();
        return null;
    }
}
