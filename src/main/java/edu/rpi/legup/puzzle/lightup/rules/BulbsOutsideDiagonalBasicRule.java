package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.rules.RuleType;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;

public class BulbsOutsideDiagonalBasicRule extends BasicRule
{

    public BulbsOutsideDiagonalBasicRule()
    {
        super("Bulbs Outside Diagonal",
                "Cells on the external edges of a 3 diagonal to a numerical block must be bulbs.",
                "edu/rpi/legup/images/lightup/rules/BulbsOutsideDiagonal.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
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
     * specific puzzleElement index using this rule and if so will perform the default application of the rule
     *
     * @param transition   transition to apply default application
     * @param puzzleElement equivalent puzzleElement
     *
     * @return true if the child node logically follow from the parent node and accepts the changes
     * to the board, otherwise false
     */
    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return false;
    }
}
