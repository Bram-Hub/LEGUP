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
public class FinishWithBulbsBasicRule extends BasicRule
{

    public FinishWithBulbsBasicRule()
    {
        super("Finish with Bulbs", "The remaining unknowns around a block must be bulbs to satisfy the number.", "images/lightup/rules/FinishWithBulbs.png");
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
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell)finalBoard.getPuzzleElement(puzzleElement);
        if(cell.getType() != LightUpCellType.BULB)
        {
            return "Modified cells must be bulbs";
        }

        Point location = cell.getLocation();

        boolean isForced = isForcedBulb(initialBoard, new Point(location.x + 1, location.y)) ||
                isForcedBulb(initialBoard, new Point(location.x, location.y + 1)) ||
                isForcedBulb(initialBoard, new Point(location.x - 1, location.y)) ||
                isForcedBulb(initialBoard, new Point(location.x, location.y - 1));

        if(isForced)
        {
            return null;
        }
        return "Bulb is not forced";
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

    private boolean isForcedBulb(LightUpBoard board, Point loc)
    {
        int availableSpots = 0;

        LightUpCell cell = board.getCell(loc.x, loc.y);
        if(cell == null || cell.getType() != LightUpCellType.NUMBER)
        {
            return false;
        }

        int bulbs = cell.getData();
        cell = board.getCell(loc.x + 1, loc.y);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x, loc.y + 1);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x - 1, loc.y);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x, loc.y - 1);
        if(cell != null)
        {
            LightUpCellType cellType = cell.getType();
            if(cellType == LightUpCellType.UNKNOWN && !cell.isLite())
            {
                availableSpots++;
            }
            else if(cellType == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        return bulbs == availableSpots;
    }
}
