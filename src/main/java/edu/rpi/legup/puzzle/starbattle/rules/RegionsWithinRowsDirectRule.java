package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

public class RegionsWithinRowsDirectRule extends DirectRule {
    public RegionsWithinRowsDirectRule() {
        super(
                "STBL-BASC-0006",
                "Regions Within Rows",
                "If a number of regions is fully contained by a number of rows with an equal number of missing stars, spaces of other regions in those rows must be black.",
                "edu/rpi/legup/images/starbattle/rules/RegionsWithinRowsDirectRule.png");
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

        RowsWithinRegionsDirectRule correspondingRule = new RowsWithinRegionsDirectRule();
        String result = correspondingRule.checkRuleRawAt(transition, puzzleElement);
        if (result != null && result.equals("Only black out cells outside the row(s)!")) {
            return "Only black out cells outside the region(s)!";
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
