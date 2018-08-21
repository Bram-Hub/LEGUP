package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;

import java.util.ArrayList;
import java.util.Set;

public class CantReachWhiteContradictionRule extends ContradictionRule
{

    public CantReachWhiteContradictionRule()
    {
        super("Cant Reach white cell",
                "A white cell must be able to reach a white region",
                "edu/rpi/legup/images/nurikabe/contradictions/CantReach.png");
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
