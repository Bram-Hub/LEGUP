package puzzle.nurikabe.rules;

import model.rules.ContradictionRule;
import model.tree.TreeTransition;
import puzzle.nurikabe.NurikabeBoard;
import puzzle.nurikabe.NurikabeCell;
import puzzle.nurikabe.NurikabeType;
import puzzle.nurikabe.NurikabeUtilities;
import utility.DisjointSets;

import java.util.ArrayList;
import java.util.Set;

public class TooManySpacesContradictionRule extends ContradictionRule
{

    public TooManySpacesContradictionRule()
    {
        super("Too Many Spaces", "A region cannot contain more spaces than its number.", "images/nurikabe/contradictions/TooManySpaces.png");
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

        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(board);
        Set<NurikabeCell> whiteRegion = regions.getSet(cell);
        ArrayList<NurikabeCell> numberedCells = new ArrayList<>();
        for(NurikabeCell c : whiteRegion)
        {
            if(c.getType() == NurikabeType.NUMBER)
            {
                numberedCells.add(c);
            }
        }

        for(NurikabeCell number : numberedCells)
        {
            if(whiteRegion.size() > number.getValueInt())
            {
                return null;
            }
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
