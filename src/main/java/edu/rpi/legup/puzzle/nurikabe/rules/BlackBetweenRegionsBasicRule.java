package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.*;
import edu.rpi.legup.utility.DisjointSets;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class BlackBetweenRegionsBasicRule extends BasicRule
{

    public BlackBetweenRegionsBasicRule()
    {
        super("Black Between Regions", "Any unknowns between two regions must be black.", "images/nurikabe/rules/BetweenRegions.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        Set<ContradictionRule> contras = new LinkedHashSet<>();
        contras.add(new MultipleNumbersContradictionRule());
        contras.add(new TooManySpacesContradictionRule());

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell)destBoardState.getPuzzleElement(puzzleElement);

        if(cell.getType() != NurikabeType.BLACK)
        {
            return "Only black cells are allowed for this rule!";
        }

        int x = cell.getLocation().x;
        int y = cell.getLocation().y;

        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(destBoardState);
        Set<NurikabeCell> adjacentWhiteRegions = new HashSet<>();
        NurikabeCell upCell = destBoardState.getCell(x, y - 1);
        NurikabeCell rightCell = destBoardState.getCell(x + 1, y);
        NurikabeCell downCell = destBoardState.getCell(x, y + 1);
        NurikabeCell leftCell = destBoardState.getCell(x - 1, y);

        if(upCell != null && (upCell.getType() == NurikabeType.WHITE || upCell.getType() == NurikabeType.NUMBER))
        {
            NurikabeCell repCell = regions.find(upCell);
            if(!adjacentWhiteRegions.contains(repCell))
            {
                adjacentWhiteRegions.add(repCell);
            }
        }
        if(rightCell != null && (rightCell.getType() == NurikabeType.WHITE || rightCell.getType() == NurikabeType.NUMBER))
        {
            NurikabeCell repCell = regions.find(rightCell);
            if(!adjacentWhiteRegions.contains(repCell))
            {
                adjacentWhiteRegions.add(repCell);
            }
        }
        if(downCell != null && (downCell.getType() == NurikabeType.WHITE || downCell.getType() == NurikabeType.NUMBER))
        {
            NurikabeCell repCell = regions.find(downCell);
            if(!adjacentWhiteRegions.contains(repCell))
            {
                adjacentWhiteRegions.add(repCell);
            }
        }
        if(leftCell != null && (leftCell.getType() == NurikabeType.WHITE || leftCell.getType() == NurikabeType.NUMBER))
        {
            NurikabeCell repCell = regions.find(leftCell);
            if(!adjacentWhiteRegions.contains(repCell))
            {
                adjacentWhiteRegions.add(repCell);
            }
        }

        if(adjacentWhiteRegions.size() < 2)
        {
            return "The new black cell must separate two white regions for this rule!";
        }

        NurikabeBoard modified = origBoardState.copy();
        modified.getCell(x, y).setData(NurikabeType.WHITE.toValue());

        for(ContradictionRule c : contras)
        {
            if(c.checkContradiction(new TreeTransition(null, modified)) == null)
            {
                return null;
            }
        }
        return "Does not follow from the rule";
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
