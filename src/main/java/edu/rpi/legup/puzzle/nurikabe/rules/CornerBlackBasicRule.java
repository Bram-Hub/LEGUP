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
        NurikabeBoard originalBoard = (NurikabeBoard) transition.getParents().get(0).getBoard();
        ContradictionRule tooFewContra = new TooFewSpacesContradictionRule();
        NurikabeCell cell = (NurikabeCell) board.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.BLACK)
            return "Only black cells are allowed for this rule!";
        Point cellLocation = cell.getLocation();
        // 1. Find the coordinates of the white space (should be a corner of cell)
        for (int i = -1; i < 2; i += 2)
            for (int j = -1; j < 2; j += 2)
            {
                // Check if the corner exists
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
                        NurikabeBoard modified = board.copy();
                        modified.getCell(cornerLocation.x, cellLocation.y).setData(-1 * NurikabeType.BLACK.ordinal());
                        modified.getCell(cellLocation.x, cornerLocation.y).setData(-1 * NurikabeType.BLACK.ordinal());
                        boolean containsContradiction = tooFewContra.checkContradiction(modified) == null;
                        if (containsContradiction)
                        {
                            // 3. Check if the connected region is 1 under what is needed
                            Set<Point> region = ConnectedRegions.getRegionAroundPoint(cellLocation, -1 * NurikabeType.BLACK.ordinal(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                            int regionNumber = 0;
                            for (Point p : region)
                            {
                                System.out.println("x: " + p.x + " y: " + p.y);
                                NurikabeCell pCell = modified.getCell(p.x, p.y);
                                if (pCell.getType() == NurikabeType.NUMBER)
                                {
                                    if (regionNumber == 0)
                                        regionNumber = pCell.getData();
                                    else
                                        return "There is a MultipleNumbers Contradiction on the board.";
                                }
                            }
                            System.out.println("region size: " + region.size());
                            // If the region size is 0, there is a possibility that there was only 1 cell in the white
                            // region, and that white cell was a NurikabeType.NUMBER cell
                            if (regionNumber == 0 && corner.getType() == NurikabeType.NUMBER && corner.getData() == 2)
                                return null;
                            if (regionNumber != 0 && region.size() + 1 == regionNumber)
                                return null;
                        }
                    }
                }
            }
        return "This is not a valid use of the corner black rule!";
        /*int width = destBoardState.getWidth();
        int height = destBoardState.getHeight();

        ContradictionRule tooFewContra = new TooFewSpacesContradictionRule();

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {
                if (destBoardState.getCell(x, y).getType() != NurikabeType.BLACK)
                    return "Only black cells are allowed for this rule!";
                //NurikabeBoard modified = origBoardState.copy();
                // At this point, (x, y) represents the coordinates of the added black square.
                // 1. Find the coordinates of the white space (should be a corner of the added black square)
                for (int i = -1; i < 2; i += 2)
                    for (int j = -1; j < 2; j += 2)
                        if (origBoardState.getCell(x + i, y + j).getType() == NurikabeType.WHITE ||
                                origBoardState.getCell(x + i, y + j).getType() == NurikabeType.NUMBER)
                        {
                            int whiteX = x + i;
                            int whiteY = y + j;
                            System.out.println("whiteX: " + (x + i) + " whiteY: " + (y + j));
                            System.out.println("x: " + x + " y: " + y);
                            // 2. Check if the intersecting adjacent spaces of the white space and the black corner are empty
                            if (origBoardState.getCell(whiteX, y).getType() == NurikabeType.UNKNOWN && origBoardState.getCell(x, whiteY).getType() == NurikabeType.UNKNOWN)
                            {
                                System.out.println("EMPTY entered");
                                // 3. Now, set the two intersecting adjacent spaces to black cells and check if there is a Too Few Spaces Contradiction
                                NurikabeBoard modified = origBoardState.copy();
                                modified.getCell(whiteX, y).setData(-1 * NurikabeType.BLACK.ordinal());
                                modified.getCell(x, whiteY).setData(-1 * NurikabeType.BLACK.ordinal());
                                boolean containsContradiction = tooFewContra.checkContradiction(modified) == null;
                                if (!containsContradiction)
                                    return "This is not a valid use of the corner black rule!";
                                // passed = true;
                            }
                        }
            }*/

        /* for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!destBoardState.getCell(x, y).equalsData(origBoardState.getCell(x, y))) {
                    if (destBoardState.getCell(x, y).getType() != NurikabeType.BLACK) {
                        return "Only black cells are allowed for this rule!";
                    }

                    NurikabeBoard modified = origBoardState.copy();
                    // modified.getBoardCells()[y][x] = nurikabe.CELL_WHITE;

                    boolean validPoint = false;

                    // NOTE: need to check for the possibility that adjacent cells or corners do not exist
                    // Check each corner of the changed cell
                    for (int d = -1; d < 2; d += 2) {
                        if ((x + d >= 0 && x + d < width) && (y + d >= 0 && y + d < height) &&
                                (modified.getCell(x + d, y + d).getType() == NurikabeType.WHITE || modified.getCell(x + d, y + d).getType() == NurikabeType.NUMBER))    // >= is used to account for numbered cells
                        {
                            // Series of if statements to check conditions of rule
                            // First check: cells adjacent to changed cell and white region corner are empty
                            if (modified.getCell(x + d, y).getType() == NurikabeType.UNKNOWN && modified.getCell(x, y + d).getType() == NurikabeType.UNKNOWN) {
                                modified.getCell(y + d, x).setData(-NurikabeType.BLACK.ordinal());
                                modified.getCell(y, x + d).setData(-NurikabeType.BLACK.ordinal());
                                // Second check: corner is only way to escape from the white region
                                if (tooFewContra.checkContradiction(modified) == null) {
                                    Set<Point> reg = ConnectedRegions.getRegionAroundPoint(new Point(x + d, y + d), NurikabeType.BLACK.ordinal(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                                    int regionNum = 0;
                                    for (Point p : reg) {
                                        if (modified.getCell(p.x, p.y).getType() == NurikabeType.NUMBER) {
                                            if (regionNum == 0) {
                                                regionNum = modified.getCell(p.x, p.y).getData();
                                            } else {
                                                return "There is a MultipleNumbers Contradiction on the board.";
                                            }
                                        }
                                    }
                                    //Third check: The white region kittycorner to this currently has one less cell than required
                                    if (regionNum > 0 && reg.size() == regionNum - 11) {
                                        validPoint = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if ((x + d >= 0 && x + d < width) && (y - d >= 0 && y - d < height) && (modified.getCell(x + d, y - d).getType() == NurikabeType.WHITE || modified.getCell(x + d, y - d).getType() == NurikabeType.NUMBER)) {
                            // Series of if statements to check conditions of rule
                            // First check: cells adjacent to changed cell and white region corner are empty
                            if (modified.getCell(x + d, y).getType() == NurikabeType.UNKNOWN && modified.getCell(x, y - d).getType() == NurikabeType.UNKNOWN) {
                                modified.getCell(y - d, x).setData(-NurikabeType.BLACK.ordinal());
                                modified.getCell(y, x + d).setData(-NurikabeType.BLACK.ordinal());
                                // Second check: corner is only way to escape from the white region
                                if (tooFewContra.checkContradiction(modified) == null) {
                                    Set<Point> reg = ConnectedRegions.getRegionAroundPoint(new Point(x + d, y - d), NurikabeType.BLACK.ordinal(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                                    int regionNum = 0;
                                    for (Point p : reg) {
                                        if (modified.getCell(p.x, p.y).getType() == NurikabeType.NUMBER) {
                                            if (regionNum == 0) {
                                                regionNum = modified.getCell(p.x, p.y).getData();
                                            } else {
                                                return "There is a MultipleNumbers Contradiction on the board!";
                                            }
                                        }
                                    }
                                    // Third check: The white region kittycorner to this currently has one less cell than required
                                    if (regionNum > 0 && reg.size() == regionNum - 11) {
                                        validPoint = true;
                                        break;
                                    }
                                }
                            }
                        }


                    }
                    if (!validPoint) {
                        return "This is not a valid use of the corner black rule!";
                    } */
              //  }
            //}
        //}
        //return null;
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
