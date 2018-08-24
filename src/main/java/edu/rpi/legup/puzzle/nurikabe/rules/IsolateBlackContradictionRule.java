package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.*;
import edu.rpi.legup.utility.DisjointSets;

import java.util.Set;

public class IsolateBlackContradictionRule extends ContradictionRule
{

    public IsolateBlackContradictionRule()
    {
        super("Isolated Black",
                "There must still be a possibility to connect every Black cell",
                "edu/rpi/legup/images/nurikabe/contradictions/BlackArea.png");
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
        NurikabeCell cell = (NurikabeCell) board.getPuzzleElement(puzzleElement);
        if(cell.getType() != NurikabeType.BLACK)
        {
            return "Contradiction must be a black cell";
        }

        DisjointSets<NurikabeCell> blackRegions = NurikabeUtilities.getPossibleBlackRegions(board);
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
}
