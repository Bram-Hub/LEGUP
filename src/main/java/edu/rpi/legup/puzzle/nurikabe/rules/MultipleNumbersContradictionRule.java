package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Element;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.*;
import edu.rpi.legup.utility.DisjointSets;

import java.util.Set;

public class MultipleNumbersContradictionRule extends ContradictionRule
{

    public MultipleNumbersContradictionRule()
    {
        super("Multiple Numbers", "All white regions cannot have more than one number.", "images/nurikabe/contradictions/MultipleNumbers.png");
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
        int height = board.getHeight();
        int width = board.getWidth();

        NurikabeCell cell = (NurikabeCell)board.getElementData(element);
        if(cell.getType() != NurikabeType.NUMBER)
        {
            return "Contradiction must be a numbered cell";
        }
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(board);
        Set<NurikabeCell> numberedRegion = regions.getSet(cell);
        for(NurikabeCell c : numberedRegion)
        {
            if(c != cell && c.getType() == NurikabeType.NUMBER)
            {
                return null;
            }
        }
        return "Does not contain a contradiction at this index";
        /*
        boolean[][] neighbors = new boolean[height][width];
        for(int x = 0; x < width; ++x)
        {
            for(int y = 0; y < height; ++y)
            {
                if(board.getCell(x, y).getType() == NurikabeType.BLACK || board.getCell(x, y).getType() == NurikabeType.UNKNOWN)
                {
                    neighbors[y][x] = true;
                }
            }
        }

        for(int x = 0; x < width; ++x)
        {
            for(int y = 0; y < height; ++y)
            {
                if(!neighbors[y][x])
                {
                    if(loopConnected(neighbors, board, x, y, width, height) > 1)
                    {
                        return null;
                    }
                }
            }
        }

        return "No regions with multiple numbers.";*/
    }

    private int loopConnected(boolean[][] neighbors, NurikabeBoard board, int x, int y, int width, int height)
    {
        int numCount = 0;
        if(board.getCell(x, y).getType() == NurikabeType.NUMBER)
        {
            numCount++;
        }
        neighbors[y][x] = true;
        if(x + 1 < width)
        {
            if(!neighbors[y][x + 1])
            {
                numCount += loopConnected(neighbors, board, x + 1, y, width, height);
            }
        }
        if(x - 1 >= 0)
        {
            if(!neighbors[y][x - 1])
            {
                numCount += loopConnected(neighbors, board, x - 1, y, width, height);
            }
        }
        if(y + 1 < height)
        {
            if(!neighbors[y + 1][x])
            {
                numCount += loopConnected(neighbors, board, x, y + 1, width, height);
            }
        }
        if(y - 1 >= 0)
        {
            if(!neighbors[y - 1][x])
            {
                numCount += loopConnected(neighbors, board, x, y - 1, width, height);
            }
        }
        return numCount;
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
