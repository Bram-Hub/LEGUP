package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Element;
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
public class MustLightBasicRule extends BasicRule
{

    public MustLightBasicRule()
    {
        super("Must Light", "A cell must be a bulb if it is the only cell to be able to light another.", "images/lightup/rules/MustLight.png");
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
        LightUpBoard initialBoard = (LightUpBoard) transition.getBoard();
        initialBoard.fillWithLight();
        LightUpBoard finalBoard = (LightUpBoard) transition.getBoard();
        LightUpCell cell = (LightUpCell)finalBoard.getElementData(element);
        if(cell.getType() != LightUpCellType.BULB)
        {
            return "Modified cells must be bulbs";
        }

        Point location = cell.getLocation();
        for(int i = location.x + 1; i < initialBoard.getWidth(); i++)
        {
            LightUpCell c = initialBoard.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.UNKNOWN && !c.isLite())
            {
                return "Cell can by lite by another cell";
            }
        }
        for(int i = location.x - 1; i >= 0; i--)
        {
            LightUpCell c = initialBoard.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.UNKNOWN && !c.isLite())
            {
                return "Cell can by lite by another cell";
            }
        }
        for(int i = location.y + 1; i < initialBoard.getHeight(); i++)
        {
            LightUpCell c = initialBoard.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.UNKNOWN && !c.isLite())
            {
                return "Cell can by lite by another cell";
            }
        }
        for(int i = location.x - 1; i >= 0; i--)
        {
            LightUpCell c = initialBoard.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.UNKNOWN && !c.isLite())
            {
                return "Cell can by lite by another cell";
            }
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
}
