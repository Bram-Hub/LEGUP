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

public class ColumnsWithinRowsDirectRule extends DirectRule {

    public ColumnsWithinRowsDirectRule() {
        super(
                "STBL-BASC-0003",
                "Columns Within Rows",
                "If a number of columns is fully contained by a number of rows with an equal number of missing stars, spaces of other columns in those rows must be black.",
                "edu/rpi/legup/images/starbattle/rules/ColumnsWithinRowsDirectRule.png");
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

        // assumption: the rule has been applied to its fullest extent and the rows and columns
        // are now mutually encompassing
        StarBattleBoard board = (StarBattleBoard) transition.getBoard();
        StarBattleCell cell = (StarBattleCell) board.getPuzzleElement(puzzleElement);
        if (cell.getType() != StarBattleCellType.BLACK) {
            return "Only black cells are allowed for this rule!";
        }

        // the columns that are contained
        Set<Integer> columns = new HashSet<Integer>();
        // the rows that contain them
        Set<Integer> rows = new HashSet<Integer>();
        // columns and rows to process
        List<Integer> columnsToCheck = new ArrayList<Integer>();
        List<Integer> rowsToCheck = new ArrayList<Integer>();
        int columnStars = 0;
        int rowStars = 0;
        int firstRow = cell.getLocation().y;
        rows.add(firstRow);
        rowsToCheck.add(firstRow);

        while (!columnsToCheck.isEmpty() || !rowsToCheck.isEmpty()) {
            for (int i = 0; i < rowsToCheck.size(); ++i) {
                int r = rowsToCheck.get(i);
                rowStars += board.rowStars(r);
                for (PuzzleElement c : board.getRow(r)) {
                    int column = ((StarBattleCell) c).getLocation().x;
                    if (columns.add(column)) {
                        columnsToCheck.add(column);
                    }
                }
                rowsToCheck.remove(i);
                --i;
            }
            for (int i = 0; i < columnsToCheck.size(); ++i) {
                int c = columnsToCheck.get(i);
                columnStars += board.columnStars(c);
                for (PuzzleElement r : board.getCol(c)) {
                    int row = ((StarBattleCell) r).getLocation().y;
                    if (rows.add(row)) {
                        rowsToCheck.add(row);
                    }
                }
                columnsToCheck.remove(i);
                --i;
            }
        }
        // are the columns and regions missing an equal amount of stars
        if (board.getPuzzleNumber() * columns.size() - columnStars
                != board.getPuzzleNumber() * rows.size() - rowStars) {
            return "The number of missing stars in the columns and rows must be equal and every extraneous cell must be black!";
        }
        if (columns.contains(cell.getLocation().x)) {
            return "Only black out cells outside the column(s)!";
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
