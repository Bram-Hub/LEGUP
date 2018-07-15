package puzzle.nurikabe.rules;

import model.rules.BasicRule;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;

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
     * @param elementIndex index of the element
     *
     * @return null if the child node logically follow from the parent node at the specified element,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, int elementIndex)
    {
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoard = (NurikabeBoard) transition.getParents().get(0).getBoard();
        ContradictionRule contraRule = new IsolateBlackContradictionRule();

        NurikabeCell cell = (NurikabeCell)board.getElementData(elementIndex);

        if(cell.getType() != NurikabeType.WHITE)
        {
            return "Only white cells are allowed for this rule!";
        }
        NurikabeBoard modified = origBoard.copy();
        modified.getElementData(elementIndex).setValueInt(NurikabeType.BLACK.toValue());
        if(contraRule.checkContradictionAt(new TreeTransition(null, modified), elementIndex) != null)
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
