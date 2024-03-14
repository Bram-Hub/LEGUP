package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;

public class FinishColumnWithEmptyDirectRule extends DirectRule {

    public FinishColumnWithEmptyDirectRule() {
        super("FCE",
                "Finish Column with Empty",
                "When a column is filled with mercury equal to the corresponding edge number, the rest are blocked.",
                "edu/rpi/legup/images/nurikabe/rules/FinishColumnWithEmpty.png");
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
        ThermometerBoard board = (ThermometerBoard) transition.getBoard();
        ThermometerBoard origBoard = (ThermometerBoard) transition.getParents().get(0).getBoard();

        // Grab the puzzleElement (Array of X'd cells? Or compare board to origBoard?)
        // Get the column that is being changed
        // Get the number of filled vials in the row
        // If the number filled != number given, throw error

        // All is well, valid
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}