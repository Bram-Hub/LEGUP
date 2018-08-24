package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

import java.awt.*;

public class TooManyBulbsContradictionRule extends ContradictionRule
{

    public TooManyBulbsContradictionRule()
    {
        super("Too Many Bulbs",
                "There cannot be more bulbs around a block than its number states.",
                "edu/rpi/legup/images/lightup/contradictions/TooManyBulbs.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check contradiction
     * @param puzzleElement index of the puzzleElement
     *
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        LightUpBoard board = (LightUpBoard) transition.getBoard();
        LightUpCell cell = (LightUpCell)board.getPuzzleElement(puzzleElement);
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
}
