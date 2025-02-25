package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

public class RowsWithinColumnsDirectRule extends DirectRule {

    public RowsWithinColumnsDirectRule() {
        super(
                "STBL-BASC-0007",
                "Rows Within Columns",
                "If a number of rows is fully contained by a number of columns with an equal number of missing stars, spaces of other rows in those columns must be black.",
                "edu/rpi/legup/images/starbattle/empty/RowsWithinColumnsDirectRule.png");
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

        ColumnsWithinRowsDirectRule correspondingRule = new ColumnsWithinRowsDirectRule();
        String result = correspondingRule.checkRuleRawAt(transition, puzzleElement);
        if (result != null && result.equals("The columns must fully fit within rows with the same number of stars missing!")) {
            return "The rows must fully fit within columns with the same number of stars missing!";
        }
        return result;
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
