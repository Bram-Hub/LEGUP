package puzzle.lightup.rules;

import model.gameboard.Element;
import model.rules.BasicRule;
import model.rules.RegisterRule;
import model.rules.RuleType;
import model.tree.TreeTransition;
import puzzle.lightup.LightUp;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;

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
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param element index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, Element element)
    {
        LightUpBoard initialBoard = (LightUpBoard)transition.getParents().get(0).getBoard();
        LightUpBoard finalBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell)finalBoard.getElementData(element);
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
     * @param element
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, Element element)
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
