package puzzle.lightup.rules;

import model.rules.ContradictionRule;
import model.rules.RegisterRule;
import model.rules.RuleType;
import model.tree.TreeTransition;
import puzzle.lightup.LightUp;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;

import java.awt.*;


@RegisterRule(puzzleName = LightUp.class, ruleType = RuleType.CONTRADICTION)
public class TooManyBulbsContradictionRule extends ContradictionRule
{

    public TooManyBulbsContradictionRule()
    {
        super("Too Many Bulbs", "There cannot be more bulbs around a block than its number states.", "images/lightup/contradictions/TooManyBulbs.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific element index using this rule
     *
     * @param transition   transition to check contradiction
     * @param elementIndex index of the element
     *
     * @return null if the transition contains a contradiction at the specified element,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, int elementIndex)
    {
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        LightUpCell cell = (LightUpCell)board.getElementData(elementIndex);
        if(cell.getType() != LightUpCellType.NUMBER)
        {
            return "Does not contain a contradiction";
        }

        Point location = cell.getLocation();

        int bulbs = 0;

        LightUpCell up = board.getCell(location.x, location.y + 1);
        if(up != null && up.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        LightUpCell down = board.getCell(location.x, location.y - 1);
        if(down != null && down.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        LightUpCell right = board.getCell(location.x + 1, location.y);
        if(right != null && right.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }
        LightUpCell left = board.getCell(location.x - 1, location.y);
        if(left != null && left.getType() == LightUpCellType.BULB)
        {
            bulbs++;
        }

        if(bulbs > cell.getData())
        {
            return null;
        }
        return "Number does not contain a contradiction";
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
}
