package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;

public class TooFewInFleetContradictionRule extends ContradictionRule {

    public TooFewInFleetContradictionRule() {
        super("Too Few in Fleet",
                "",
                "edu/rpi/legup/images/battleship/contradictions/too_few_in_fleet.png");
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
