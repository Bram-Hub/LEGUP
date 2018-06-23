package puzzle.nurikabe.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.*;
import utility.DisjointSet;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class IsolateBlackContradictionRule extends ContradictionRule
{

    public IsolateBlackContradictionRule()
    {
        super("Isolated Black", "There must still be a possibility to connect every Black cell", "images/nurikabe/contradictions/BlackArea.png");
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
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = (NurikabeCell) board.getElementData(elementIndex);
        if(cell.getType() != NurikabeType.BLACK)
        {
            return "Contradiction must be a black cell";
        }

        DisjointSet<NurikabeCell> blackRegions = NurikabeUtilities.getPossibleBlackRegions(board);
        boolean oneRegion = false;
        for(Set<NurikabeCell> region : blackRegions.getAllSets())
        {
            for(NurikabeCell c : region)
            {
                if(c.getType() == NurikabeType.BLACK)
                {
                    if(oneRegion)
                    {
                        return null;
                    }
                    else
                    {
                        oneRegion = true;
                        break;
                    }
                }
            }
        }
        return "Contradiction applied incorrectly. No isolated Blacks.";
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
