package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.utility.ConnectedRegions;

import java.awt.*;
import java.util.Set;

public class CornerBlackBasicRule extends BasicRule
{

    public CornerBlackBasicRule()
    {
        super("Corners Black", "If there is only one white square connected to unknowns and one more white is needed then the angles of that white square are black", "images/nurikabe/rules/CornerBlack.png");
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
        NurikabeBoard destBoardState = (NurikabeBoard)transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        int width = destBoardState.getWidth();
        int height = destBoardState.getHeight();

        ContradictionRule tooFewContra = new TooFewSpacesContradictionRule();

        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(!destBoardState.getCell(x, y).equalsData(origBoardState.getCell(x, y)))
                {
                    if(destBoardState.getCell(x, y).getData() != NurikabeType.BLACK.ordinal())
                    {
                        return "Only black cells are allowed for this rule!";
                    }

                    NurikabeBoard modified = origBoardState.copy();
                    // modified.getBoardCells()[y][x] = nurikabe.CELL_WHITE;

                    boolean validPoint = false;

                    // Check each corner of the changed cell
                    for(int d = -1; d < 2; d += 2)
                    {
                        if((x + d >= 0 && x + d < width) && (y + d >= 0 && y + d < height) && modified.getCell(x + d, y + d).getData() >= NurikabeType.WHITE.ordinal())    // >= is used to account for numbered cells
                        {
                            // Series of if statements to check conditions of rule
                            // First check: cells adjacent to changed cell and white region corner are empty
                            if(modified.getCell(x + d, y).getData() == NurikabeType.UNKNOWN.ordinal() && modified.getCell(x, y + d).getData() == NurikabeType.UNKNOWN.ordinal())
                            {
                                modified.getCell(y + d, x).setData(NurikabeType.BLACK.ordinal());
                                modified.getCell(y, x + d).setData(NurikabeType.BLACK.ordinal());
                                // Second check: corner is only way to escape from the white region
                                if(tooFewContra.checkContradiction(new TreeTransition(null, modified)) == null)
                                {
                                    Set<Point> reg = ConnectedRegions.getRegionAroundPoint(new Point(x + d, y + d), NurikabeType.BLACK.ordinal(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                                    int regionNum = 0;
                                    for(Point p : reg)
                                    {
                                        if(modified.getCell(p.x, p.y).getType() == NurikabeType.NUMBER)
                                        {
                                            if(regionNum == 0)
                                            {
                                                regionNum = modified.getCell(p.x, p.y).getData();
                                            }
                                            else
                                            {
                                                return "There is a MultipleNumbers Contradiction on the board.";
                                            }
                                        }
                                    }
                                    //Third check: The white region kittycorner to this currently has one less cell than required
                                    if(regionNum > 0 && reg.size() == regionNum - 11)
                                    {
                                        validPoint = true;
                                        break;
                                    }
                                }
                            }
                        }

                        if((x + d >= 0 && x + d < width) && (y - d >= 0 && y - d < height) && modified.getCell(x + d, y - d).getData() >= NurikabeType.WHITE.ordinal())
                        {
                            // Series of if statements to check conditions of rule
                            // First check: cells adjacent to changed cell and white region corner are empty
                            if(modified.getCell(x + d, y).getData() == NurikabeType.UNKNOWN.ordinal() && modified.getCell(x, y - d).getData() == NurikabeType.UNKNOWN.ordinal())
                            {
                                modified.getCell(y - d, x).setData(NurikabeType.BLACK.ordinal());
                                modified.getCell(y, x + d).setData(NurikabeType.BLACK.ordinal());
                                // Second check: corner is only way to escape from the white region
                                if(tooFewContra.checkContradiction(new TreeTransition(null, modified)) == null)
                                {
                                    Set<Point> reg = ConnectedRegions.getRegionAroundPoint(new Point(x + d, y - d), NurikabeType.BLACK.ordinal(), modified.getIntArray(), modified.getWidth(), modified.getHeight());
                                    int regionNum = 0;
                                    for(Point p : reg)
                                    {
                                        if(modified.getCell(p.x, p.y).getType() == NurikabeType.NUMBER)
                                        {
                                            if(regionNum == 0)
                                            {
                                                regionNum = modified.getCell(p.x, p.y).getData();
                                            }
                                            else
                                            {
                                                return "There is a MultipleNumbers Contradiction on the board!";
                                            }
                                        }
                                    }
                                    // Third check: The white region kittycorner to this currently has one less cell than required
                                    if(regionNum > 0 && reg.size() == regionNum - 11)
                                    {
                                        validPoint = true;
                                        break;
                                    }
                                }
                            }
                        }


                    }
                    if(!validPoint)
                    {
                        return "This is not a valid use of the corner black rule!";
                    }
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
