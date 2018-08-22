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

public class FinishWithEmptyBasicRule extends BasicRule
{

    public FinishWithEmptyBasicRule()
    {
        super("Finish with Empty",
                "The remaining unknowns around a block must be empty if the number is satisfied.",
                "edu/rpi/legup/images/lightup/rules/FinishWithEmpty.png");
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
