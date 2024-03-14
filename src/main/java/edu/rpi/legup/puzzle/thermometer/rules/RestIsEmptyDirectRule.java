package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;

public class RestIsEmptyDirectRule extends DirectRule {

    public RestIsEmptyDirectRule() {
        super("RIE",
                "Rest Is Empty",
                "If mercury is blocked at a non-tail section, the rest of the thermometer is also blocked.",
                "edu/rpi/legup/images/nurikabe/rules/RestIsEmpty.png");
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
        // Ensure we only X'd a single vial, else return error
        // Find the closest cell to the head of the vial
        // If vertical, check to ensure # of filled cells in that row = outside #, else return error
        // If horizontal, check to ensure # of filled cells in that column = outside #, else return error
        // Make sure every vial from the closest to head cell to the tail is X'd, else return error

        // All is well, valid
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}