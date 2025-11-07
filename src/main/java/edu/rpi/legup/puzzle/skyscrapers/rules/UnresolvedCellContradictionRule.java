package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import java.awt.*;
import java.util.ArrayList;

public class UnresolvedCellContradictionRule extends ContradictionRule {

    public UnresolvedCellContradictionRule() {
        super(
                "SKYS-CONT-0004",
                "Unresolved Cell",
                "Elimination leaves no possible number for a cell.",
                "edu/rpi/legup/images/skyscrapers/contradictions/UnresolvedCell.png");
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

        NumberForCellCaseRule caseRule = new NumberForCellCaseRule();
        ArrayList<Board> cases = caseRule.getCases(board, puzzleElement);

        if (cases.size() == 0) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
