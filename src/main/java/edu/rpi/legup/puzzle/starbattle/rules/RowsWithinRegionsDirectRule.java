package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RowsWithinRegionsDirectRule extends DirectRule {
    public RowsWithinRegionsDirectRule() {
        super(
                "STBL-BASC-0008",
                "Rows Within Regions",
                "If a number of rows is fully contained by a number of regions with an equal number of missing stars, spaces of other rows in those regions must be black.",
                "edu/rpi/legup/images/starbattle/rules/RowsWithinRegionsDirectRule.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {

        // assumption: the rule has been applied to its fullest extent and the rows and regions
        // are now mutually encompassing
        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);
        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }
        // the rows that are contained
        Set<Integer> rows = new HashSet<Integer>();
        // the regions that contain them
        Set<Integer> regions = new HashSet<Integer>();
        // rows and regions to process
        List<Integer> rowsToCheck = new ArrayList<Integer>();
        List<Integer> regionsToCheck = new ArrayList<Integer>();
        int rowStars = 0;
        int regionStars = 0;
        regions.add(cell.getGroupIndex());
        regionsToCheck.add(cell.getGroupIndex());

        while (!rowsToCheck.isEmpty() || !regionsToCheck.isEmpty()) {
            for (int i = 0; i < regionsToCheck.size(); ++i) {
                int r = regionsToCheck.get(i);
                regionStars += board.getRegion(r).numStars();
                for (StarBattleCell ro : board.getRegion(r).getCells()) {
                    int row = ro.getLocation().y;
                    if (ro.getType() == StarBattleCellType.UNKNOWN && rows.add(row)) {
                        rowsToCheck.add(row);
                    }
                }
                regionsToCheck.remove(i);
                --i;
            }
            for (int j = 0; j < rowsToCheck.size(); ++j) {
                int r = rowsToCheck.get(j);
                rowStars += board.rowStars(r);
                for (int i = 0; i < board.getSize(); ++i) {
                    int region = board.getCell(i, r).getGroupIndex();
                    if (board.getCell(i,r).getType() == StarBattleCellType.UNKNOWN && regions.add(region)) {
                        regionsToCheck.add(region);
                    }
                }
                rowsToCheck.remove(j);
                --j;
            }
        }
        // are the columns and regions missing an equal amount of stars
        if (board.getPuzzleNumber() * rows.size() - rowStars
                != board.getPuzzleNumber() * regions.size() - regionStars) {
            return "The number of missing stars in the rows and regions must be equal and every extraneous cell must be black!";
        }
        if (rows.contains(cell.getLocation().y)) {
            return "Only black out cells outside the row(s)!";
        }
        return null;
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {

        return null;
    }
}
