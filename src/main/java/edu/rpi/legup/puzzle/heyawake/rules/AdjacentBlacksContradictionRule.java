package edu.rpi.legup.puzzle.heyawake.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class AdjacentBlacksContradictionRule extends ContradictionRule {

    public AdjacentBlacksContradictionRule() {
        super(
                "HEYA-CONT-0001",
                "Adjacent Blacks",
                "",
                "edu/rpi/legup/images/heyawake/contradictions/adjacentBlacks.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        return null;
    }
}
