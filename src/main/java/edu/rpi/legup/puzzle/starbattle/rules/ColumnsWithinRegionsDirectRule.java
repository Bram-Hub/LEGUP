package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import io.opencensus.trace.Link;

import java.util.*;

public class ColumnsWithinRegionsDirectRule extends DirectRule {
    public ColumnsWithinRegionsDirectRule() {
        super(
                "STBL-BASC-0002",
                "Columns Within Regions",
                "If a number of columns is fully contained by a number of regions with an equal number of missing stars, spaces of other columns in those regions must be black.",
                "edu/rpi/legup/images/starbattle/empty/ColumnsWithinRegionsDirectRule.png");
    }

    private void generateSubsets(List<List<Integer>> subsets, int current, int skip, int size) {
        if (current == size) {
            return;
        }
        List<List<Integer>> newSubsets = new LinkedList<List<Integer>>();
        if (current != skip) {
            for (List<Integer> subset: subsets) {
                List<Integer> copy = new LinkedList<Integer>(subset);
                copy.add(current);
                newSubsets.add(copy);
            }
            subsets.addAll(newSubsets);
            List<Integer> oneMember = new LinkedList<Integer>();
            oneMember.add(current);
            subsets.add(oneMember);
        }
        generateSubsets(subsets, current + 1 == skip ? current + 2 : current + 1, skip, size);
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

        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleBoard origBoard = (StarBattleBoard) transition.getParents().get(0).getBoard();
        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);
        int dim = board.getSize();
        int region = cell.getGroupIndex();
        int column = cell.getLocation().x;

        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        List<List<Integer>> subsets = new LinkedList<List<Integer>>();
        generateSubsets(subsets,0, column, dim);

        for (List<Integer> columnSubset: subsets) {
            Set<Integer> regions = new HashSet<Integer>();
            boolean containsRegion = false;
            int columnStars = 0;
            int regionStars = 0;
            for (int c: columnSubset) {
                columnStars += origBoard.columnStars(c);
                for (StarBattleCell ce: origBoard.getCol(c)) {
                    if (ce.getType() == StarBattleCellType.UNKNOWN) {
                        if (regions.add(ce.getGroupIndex())) {
                            regionStars += origBoard.getRegion(ce.getGroupIndex()).numStars();
                        }
                        if (ce.getGroupIndex() == region) {
                            containsRegion = true;
                        }
                    }
                }
            }
            if (containsRegion && board.getPuzzleNumber() * columnSubset.size() - columnStars
                    >= board.getPuzzleNumber() * regions.size() - regionStars) {
                return null;
            }
        }
        return "The columns must fully fit within regions with the same number of stars missing!";
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
