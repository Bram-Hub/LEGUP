package puzzle.lightup.rules;

import model.rules.BasicRule;
import model.tree.TreeTransition;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;

import java.awt.*;

public class FinishWithBulbsBasicRule extends BasicRule
{

    public FinishWithBulbsBasicRule()
    {
        super("Finish with Bulbs", "The remaining unknowns around a block must be bulbs to satisfy the number.", "images/lightup/rules/FinishWithBulbs.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        LightUpBoard initialBoard = (LightUpBoard)transition.getParentNode().getBoard();
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell)finalBoard.getElementData(elementIndex);
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
     * specific element index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param elementIndex
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex)
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

        int bulbs = cell.getValueInt();
        cell = board.getCell(loc.x + 1, loc.y);
        if(cell != null)
        {
            if(cell.getType() == LightUpCellType.UNKNOWN)
            {
                availableSpots++;
            }
            else if(cell.getType() == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x, loc.y + 1);
        if(cell != null)
        {
            if(cell.getType() == LightUpCellType.UNKNOWN)
            {
                availableSpots++;
            }
            else if(cell.getType() == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x - 1, loc.y);
        if(cell != null)
        {
            if(cell.getType() == LightUpCellType.UNKNOWN)
            {
                availableSpots++;
            }
            else if(cell.getType() == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        cell = board.getCell(loc.x, loc.y - 1);
        if(cell != null)
        {
            if(cell.getType() == LightUpCellType.UNKNOWN)
            {
                availableSpots++;
            }
            else if(cell.getType() == LightUpCellType.BULB)
            {
                bulbs--;
            }
        }
        return bulbs == availableSpots;
    }
}
