package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.*;
import edu.rpi.legup.utility.DisjointSets;

import java.util.Set;

public class TooFewSpacesContradictionRule extends ContradictionRule
{

    public TooFewSpacesContradictionRule()
    {
        super("Too Few Spaces",
                "A region cannot contain less spaces than its number.",
                "edu/rpi/legup/images/nurikabe/contradictions/TooFewSpaces.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check contradiction
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();

        NurikabeCell cell = (NurikabeCell)board.getPuzzleElement(puzzleElement);
        if(cell.getType() != NurikabeType.WHITE && cell.getType() != NurikabeType.NUMBER)
        {
            return "Contradiction must be a white or a numbered cell";
        }

        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getPossibleWhiteRegions(board);
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

        if(numberedCell != null && whiteRegion.size() < numberedCell.getData())
        {
            System.err.println("Cell Value: " + numberedCell.getData() + ", Loc: " + cell.getLocation() + ", region: " + whiteRegion.size());
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
