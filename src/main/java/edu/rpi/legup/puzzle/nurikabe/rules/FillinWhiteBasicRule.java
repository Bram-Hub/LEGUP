package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;

public class FillinWhiteBasicRule extends BasicRule
{

    public FillinWhiteBasicRule()
    {
        super("Fill In White", "If there an unknown region surrounded by white, it must be white.", "images/nurikabe/rules/FillInWhite.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param element equivalent element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, Element element)
    {
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoard = (NurikabeBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new IsolateBlackContradictionRule();

        NurikabeCell cell = (NurikabeCell)board.getElementData(element);

        if(cell.getType() != NurikabeType.WHITE)
        {
            return "Only white cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoard.copy();
        modified.getElementData(element).setData(NurikabeType.BLACK.toValue());
        if(contraRule.checkContradictionAt(new TreeTransition(null, modified), element) != null)
        {
            return "white cells must be placed in a region of white cells!";
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
     * @param element equivalent element
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
