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
public class EmptyCornersBasicRule extends BasicRule
{

    public EmptyCornersBasicRule()
    {
        super("Empty Corners", "Cells on the corners of a number must be empty if placing bulbs would prevent the number from being satisfied.", "images/lightup/rules/EmptyCorners.png");
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
        LightUpBoard initialBoard = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell) initialBoard.getElementData(element);
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
