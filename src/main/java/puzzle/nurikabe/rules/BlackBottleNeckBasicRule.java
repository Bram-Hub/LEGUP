package puzzle.nurikabe.rules;

import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;

public class BlackBottleNeckBasicRule extends BasicRule
{

    public BlackBottleNeckBasicRule()
    {
        super("Black Bottle Neck", "If there is only one path for a black to escape, then those unknowns must be white.", "images/nurikabe/rules/OneUnknownBlack.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific element index using this rule
     *
     * @param transition   transition to check
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        ContradictionRule contraRule = new IsolateBlackContradictionRule();

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell) destBoardState.getElementData(elementIndex);

        if(cell.getType() != NurikabeType.BLACK)
        {
            return "Only black cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoardState.copy();
        NurikabeCell modCell = (NurikabeCell) modified.getElementData(elementIndex);
        modCell.setData(NurikabeType.WHITE.toValue());

        if(contraRule.checkContradiction(new TreeTransition(null, modified)) == null)
        {
            return null;
        }
        else
        {
            return "This is not the only way for black to escape!";
        }
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
