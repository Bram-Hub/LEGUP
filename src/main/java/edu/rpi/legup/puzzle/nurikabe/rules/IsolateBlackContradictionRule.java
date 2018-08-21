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
