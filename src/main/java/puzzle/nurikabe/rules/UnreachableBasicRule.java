package puzzle.nurikabe.rules;

import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;

public class UnreachableBasicRule extends BasicRule
{
    public UnreachableBasicRule()
    {
        super("Unreachable white region","A cell must be black if it cannot be reached by any white region","images/nurikabe/rules/Unreachable.png");
    }

    @Override
    protected String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        ContradictionRule contraRule = new CantReachWhiteContradictionRule();

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

    @Override
    public boolean doDefaultApplication(TreeTransition transition)
    {
        return false;
    }

    @Override
    public boolean doDefaultApplicationAt(TreeTransition transition, int elementIndex)
    {
        return false;
    }
}
