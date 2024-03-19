package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridRegion;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;

import java.util.HashSet;
import java.util.Set;

public class ColumnsWithinRegionsDirectRule extends DirectRule {
    public ColumnsWithinRegionsDirectRule() {
        super("STBL-BASC-0002",
                "Columns Within Regions",
                "If a number of columns is fully contained by an equal number of regions, spaces of other columns in those regions must be black.",
                "INSERT IMAGE NAME HERE");
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
        //TODO: fix this
        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);
        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }
        //which columns are within the region
        Set<Integer> columns = new HashSet<Integer>();
        int columnStars = 0;
        for (PuzzleElement r: board.getRegion(cell).getCells()) {
            int column = ((StarBattleCell) r).getLocation().x;
            if (columns.add(column)) {
                columnStars += board.columnStars(column);
            }
        }
        //which regions are the columns within
        Set<Integer> regions = new HashSet<Integer>();
        int regionStars = 0;
        for (Integer c: columns) {
            for (int i = 0; i < board.getSize(); ++i) {
                if (regions.add(board.getCell(c,i).getGroupIndex())) {
                    regionStars += board.getRegion(board.getCell(c,i)).numStars();
                }
            }
        }
        // are the columns and regions missing an equal amount of stars
        if (board.getPuzzleNumber() * columns.size() - columnStars != board.getPuzzleNumber() * regions.size() - regionStars) {
            return "The number of missing stars in the ";
        }
        return null;
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
