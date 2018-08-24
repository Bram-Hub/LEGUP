package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import edu.rpi.legup.utility.DisjointSets;

import java.util.Set;

public class NoNumberContradictionRule extends ContradictionRule
{

    public NoNumberContradictionRule()
    {
        super("No Number",
                "All enclosed white regions must have a number.",
                "edu/rpi/legup/images/nurikabe/contradictions/NoNumber.png");
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
        if(cell.getType() != NurikabeType.WHITE)
        {
            return "Contradiction must be a white cell";
        }
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(board);
        Set<NurikabeCell> whiteRegion = regions.getSet(cell);
        for(NurikabeCell c : whiteRegion)
        {
            if(c.getType() == NurikabeType.NUMBER)
            {
                return "Does not contain a contradiction at this index";
            }
        }
        return null;
    }
}
