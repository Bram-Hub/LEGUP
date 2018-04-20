package puzzle.nurikabe.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.*;
import utility.DisjointSet;

import java.awt.*;
import java.util.Set;

public class TooFewSpacesContradictionRule extends ContradictionRule
{

    public TooFewSpacesContradictionRule()
    {
        super("Too Few Spaces", "A region cannot contain less spaces than its number.", "images/nurikabe/contradictions/TooFewSpaces.png");
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

        NurikabeCell cell = (NurikabeCell)board.getElementData(elementIndex);
        if(cell.getType() != NurikabeType.WHITE && cell.getType() != NurikabeType.NUMBER)
        {
            return "Contradiction must be a white or a numbered cell";
        }

        DisjointSet<NurikabeCell> regions = NurikabeUtilities.getPossibleWhiteRegions(board);
        Set<NurikabeCell> whiteRegion = regions.getSet(cell);
        NurikabeCell numberedCell = null;
        for(NurikabeCell c : whiteRegion)
        {
            if(c.getType() == NurikabeType.NUMBER)
            {
                numberedCell = c;
                break;
            }
        }

        if(numberedCell != null && whiteRegion.size() < numberedCell.getValueInt())
        {
            System.err.println("Cell Value: " + numberedCell.getValueInt() + ", Loc: " + cell.getLocation() + ", region: " + whiteRegion.size());
            return null;
        }
        return "Does not contain a contradiction at this index";
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
