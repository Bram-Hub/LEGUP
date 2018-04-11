package puzzle.lightup.rules;

import model.rules.ContradictionRule;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import puzzle.lightup.LightUpBoard;
import puzzle.lightup.LightUpCell;
import puzzle.lightup.LightUpCellType;

import java.awt.*;

public class BulbsInPathContradictionRule extends ContradictionRule
{

    public BulbsInPathContradictionRule()
    {
        super("Bulbs In Path", "A bulb cannot be placed in another bulb's path.", "images/lightup/contradictions/BulbsInPath.png");
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
        LightUpBoard board = (LightUpBoard)transition.getBoard();
        LightUpCell cell = (LightUpCell)board.getElementData(elementIndex);
        if(cell.getType() != LightUpCellType.BULB)
        {
            return "Does not contain a contradiction at this index";
        }

        Point location = cell.getLocation();
        for(int i = location.x + 1; i < board.getWidth(); i++)
        {
            LightUpCell c = board.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.BULB)
            {
                return null;
            }
        }
        for(int i = location.x - 1; i >= 0; i--)
        {
            LightUpCell c = board.getCell(i, location.y);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.BULB)
            {
                return null;
            }
        }
        for(int i = location.y + 1; i < board.getHeight(); i++)
        {
            LightUpCell c = board.getCell(location.x, i);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.BULB)
            {
                return null;
            }
        }
        for(int i = location.y - 1; i >= 0; i--)
        {
            LightUpCell c = board.getCell(location.x, i);
            if(c.getType() == LightUpCellType.BLACK || c.getType() == LightUpCellType.NUMBER)
            {
                break;
            }
            else if(c.getType() == LightUpCellType.BULB)
            {
                return null;
            }
        }
        return "Does not contain a contradiction at this index";
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
