package puzzle.nurikabe.rules;

import model.gameboard.Element;
import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;
import puzzle.nurikabe.NurikabeUtilities;

import java.util.ArrayList;
import java.util.Set;

public class CantReachWhiteContradictionRule extends ContradictionRule
{

    public CantReachWhiteContradictionRule()
    {
        super("Cant Reach white cell", "A white cell must be able to reach a white region", "images/nurikabe/contradictions/CantReach.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific element index using this rule
     *
     * @param transition   transition to check contradiction
     * @param element equivalent element
     *
     * @return null if the transition contains a contradiction at the specified element,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, Element element)
    {
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = (NurikabeCell)board.getElementData(element);
        if(cell.getType() != NurikabeType.WHITE)
        {
            return "Does not contain a contradiction at this index";
        }

        ArrayList<Set<NurikabeCell>> regions = NurikabeUtilities.getFloodFillWhite(board);

        for(Set<NurikabeCell> region : regions)
        {
            for(NurikabeCell c : region)
            {
                if(c == cell)
                {
                    return "Cell at this index can be reached";
                }
            }
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
