package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.utility.ConnectedRegions;

import java.awt.*;
import java.util.Set;

public class CornerBlackBasicRule extends BasicRule {

    public CornerBlackBasicRule() {
        super("Corners Black",
                "If there is only one white square connected to unknowns and one more white is needed then the angles of that white square are black",
                "edu/rpi/legup/images/nurikabe/rules/CornerBlack.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        NurikabeCell cell = (NurikabeCell) board.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.BLACK)
            return "Only black cells are allowed for this rule!";

        ContradictionRule tooFewContra = new TooFewSpacesContradictionRule();
        Point cellLocation = cell.getLocation();
        // 1. Find the coordinates of the white space (should be a corner of cell)
        for (int i = -1; i < 2; i += 2)
            for (int j = -1; j < 2; j += 2)
            {
                // If the corner does not exist, skip the corner
                if (!(cellLocation.x + i >= 0 && cellLocation.x + i < board.getWidth() && cellLocation.y + j >= 0 && cellLocation.x + i < board.getHeight()))
                    continue;

                NurikabeCell corner = board.getCell(cellLocation.x + i, cellLocation.y + j);
                NurikabeType cornerType = corner.getType();
                if (cornerType == NurikabeType.WHITE || cornerType == NurikabeType.NUMBER)
                {
                    Point cornerLocation = corner.getLocation();
                    // 2. Check if the intersecting adjacent spaces of the white space and the black corner are empty
                    if (board.getCell(cornerLocation.x, cellLocation.y).getType() == NurikabeType.UNKNOWN && board.getCell(cellLocation.x, cornerLocation.y).getType() == NurikabeType.UNKNOWN)
                    {
                        // System.out.println("Went inside if statement");
                        NurikabeBoard modified = board.copy();
                        modified.getCell(cornerLocation.x, cellLocation.y).setData(NurikabeType.BLACK.toValue());
                        modified.getCell(cellLocation.x, cornerLocation.y).setData(NurikabeType.BLACK.toValue());
                        boolean containsContradiction = tooFewContra.checkContradiction(modified) == null;
                        if (containsContradiction)
                        {
                            // 3. Check if the connected region is 1 under what is needed
                            Set<Point> region = ConnectedRegions.getRegionAroundPoint(cornerLocation, NurikabeType.BLACK.toValue(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                            int regionNumber = 0;
                            // System.out.println("Region set size: " + region.size());
                            for (Point p : region)
                            {
                                NurikabeCell pCell = modified.getCell(p.x, p.y);
                                if (pCell.getType() == NurikabeType.NUMBER)
                                {
                                    if (regionNumber == 0)
                                        regionNumber = pCell.getData();
                                    else
                                        return "There is a MultipleNumbers Contradiction on the board.";
                                }
                            }
                            // If the region size is 0, there is a possibility that there was only 1 cell in the white
                            // region, and that white cell was a NurikabeType.NUMBER cell
                            if (regionNumber == 0 && corner.getType() == NurikabeType.NUMBER && corner.getData() == 2)
                                return null;
                            // If the region size is not 0, make sure the regionNumber and the region size match (need
                            // to add 1 to account for the cell that was surrounded
                            if (regionNumber != 0 && region.size() + 1 == regionNumber)
                                return null;
                        }
                    }
                }
            }
        return "This is not a valid use of the corner black rule!";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
