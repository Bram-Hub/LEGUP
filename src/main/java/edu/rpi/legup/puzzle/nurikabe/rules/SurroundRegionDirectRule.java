package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import edu.rpi.legup.utility.DisjointSets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.*;

public class SurroundRegionDirectRule extends DirectRule {
    private final static Logger LOGGER = LogManager.getLogger("");

    public SurroundRegionDirectRule() {
        super("NURI-BASC-0007", "Surround Region",
                "Surround Region",
                "edu/rpi/legup/images/nurikabe/rules/SurroundBlack.png");
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
        //ContradictionRule contraRule = new TooManySpacesContradictionRule();

        NurikabeBoard destBoardState = (NurikabeBoard) transition.getBoard();
        NurikabeBoard origBoardState = (NurikabeBoard) transition.getParents().get(0).getBoard();

        NurikabeCell cell = (NurikabeCell) destBoardState.getPuzzleElement(puzzleElement);

        if (cell.getType() != NurikabeType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        NurikabeBoard modified = origBoardState.copy();
        NurikabeCell modCell = (NurikabeCell) modified.getPuzzleElement(puzzleElement);
        modCell.setData(NurikabeType.WHITE.toValue());

//        if (contraRule.checkContradiction(modified) == null) {
//            return null;
//        }
        if(cell.getType() == NurikabeType.BLACK) {
            DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(destBoardState);
            Set<NurikabeCell> adj = new HashSet<>();
            Point loc = cell.getLocation();
            NurikabeCell cellLeft = destBoardState.getCell(loc.x - 1, loc.y);
            NurikabeCell cellRight = destBoardState.getCell(loc.x + 1, loc.y);
            NurikabeCell cellTop = destBoardState.getCell(loc.x, loc.y + 1);
            NurikabeCell cellBottom = destBoardState.getCell(loc.x, loc.y - 1);
            if(cellLeft != null) {
                if (cellLeft.getType() == NurikabeType.WHITE || cellLeft.getType() == NurikabeType.NUMBER) {
                    adj.add(cellLeft);
                }
            }
            if(cellRight != null) {
                if (cellRight.getType() == NurikabeType.WHITE || cellRight.getType() == NurikabeType.NUMBER) {
                    adj.add(cellRight);
                }
            }
            if(cellTop != null) {
                if (cellTop.getType() == NurikabeType.WHITE || cellTop.getType() == NurikabeType.NUMBER) {
                    adj.add(cellTop);
                }
            }
            if(cellBottom != null) {
                if (cellBottom.getType() == NurikabeType.WHITE || cellBottom.getType() == NurikabeType.NUMBER) {
                    adj.add(cellBottom);
                }
            }

            Set<NurikabeCell> whiteRegion;// = regions.getSet(cell); //getting set of black blocks, need to be white blocks
            whiteRegion = adj;
            ArrayList<NurikabeCell> numberedCells = new ArrayList<>();
            LOGGER.debug("anything fake");
            LOGGER.debug("white size" + whiteRegion.size());
            for (NurikabeCell c : whiteRegion) {
                Set<NurikabeCell> disRow = regions.getSet(c);
                for (NurikabeCell d : disRow) {
                    LOGGER.debug("type of block" + d.getType());
                    if (d.getType() == NurikabeType.NUMBER) {
                        LOGGER.debug("anything real");
                        numberedCells.add(d);
                    }
                }
            }
            for (NurikabeCell number : numberedCells) {
                System.out.println("number data" + number.getData());
                System.out.println("white region size" + whiteRegion.size());
                LOGGER.debug("number Data" + number.getData());
                LOGGER.debug("white region size" + whiteRegion.size());
                if (regions.getSet(number).size() == number.getData()) {
                    return null;
                }
            }
        }
        //else {
            return "Does not follow from this rule at this index";
        //}
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
