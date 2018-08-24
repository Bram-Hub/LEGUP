package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;

public class AdjacentShipsContradictionRule extends ContradictionRule {

    public AdjacentShipsContradictionRule() {
        super("Adjacent Ships",
                "",
                "edu/rpi/legup/images/battleship/contradictions/AdjacentShips.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
