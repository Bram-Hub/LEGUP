package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;


@RegisterRule(puzzleName = LightUp.class, ruleType = RuleType.BASIC)
public class EmptyCornersBasicRule extends BasicRule
{

    public EmptyCornersBasicRule()
    {
        super("Empty Corners", "Cells on the corners of a number must be empty if placing bulbs would prevent the number from being satisfied.", "images/lightup/rules/EmptyCorners.png");
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
        LightUpBoard initialBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        if(cell == null || cell.getType() != LightUpCellType.EMPTY)
        {
            return "Must be an empty cell";
        }



        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node using this rule
     * and if so will perform the default application of the rule
     *
     * @param transition transition to apply default application
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
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
}
