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
public class FinishWithEmptyBasicRule extends BasicRule
{

    public FinishWithEmptyBasicRule()
    {
        super("Finish with Empty", "The remaining unknowns around a block must be empty if the number is satisfied.", "images/lightup/rules/FinishWithEmpty.png");
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
        if(cell.getType() != LightUpCellType.EMPTY)
        {
            return "Modified cells must be empty";
        }

        Point location = cell.getLocation();

        boolean isForced = isForcedEmpty(initialBoard, new Point(location.x + 1, location.y)) ||
                isForcedEmpty(initialBoard, new Point(location.x, location.y + 1)) ||
                isForcedEmpty(initialBoard, new Point(location.x - 1, location.y)) ||
                isForcedEmpty(initialBoard, new Point(location.x, location.y - 1));

        if(isForced)
        {
            return null;
        }
        return "Empty is not forced";
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

    private boolean isForcedEmpty(LightUpBoard board, Point loc)
    {
        LightUpCell cell = board.getCell(loc.x, loc.y);
        if(cell == null || cell.getType() != LightUpCellType.NUMBER)
        {
            return false;
        }

        int bulbs = 0;
        int bulbsNeeded = cell.getData();
        cell = board.getCell(loc.x + 1, loc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(loc.x, loc.y + 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(loc.x - 1, loc.y);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        cell = board.getCell(loc.x, loc.y - 1);
        if(cell != null && cell.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        return bulbs == bulbsNeeded;
    }
}
