package puzzle.nurikabe.rules;

import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;

import java.util.LinkedHashSet;
import java.util.Set;

public class WhiteBottleNeckBasicRule extends BasicRule
{

    public WhiteBottleNeckBasicRule()
    {
        super("White Bottle Neck", "If a region needs more whites and there is only one path for the region to expand, then those unknowns must be white.", "images/nurikabe/rules/OneUnknownWhite.png");
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
        Set<ContradictionRule> contras = new LinkedHashSet<>();
        contras.add(new NoNumberContradictionRule());
        contras.add(new TooFewSpacesContradictionRule());

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell) destBoardState.getElementData(elementIndex);

        if(cell.getType() != NurikabeType.WHITE)
        {
            return "Only white cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoardState.copy();
        NurikabeCell modCell = (NurikabeCell) modified.getElementData(elementIndex);
        modCell.setValueInt(NurikabeType.BLACK.toValue());

        for(ContradictionRule contraRule : contras)
        {
            if(contraRule.checkContradiction(new TreeTransition(null, modified)) == null)
            {
                return null;
            }
        }
        return "This is not the only way for white to escape!";
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
