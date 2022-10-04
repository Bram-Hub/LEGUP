package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;

import java.util.ArrayList;
import java.util.Set;

public class UnreachableWhiteCellContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Cell at this index can be reached";
    private final String INVALID_USE_MESSAGE = "Does not contain a contradiction at this index";

    public UnreachableWhiteCellContradictionRule() {
        super("NURI-CONT-0002",
                "Unreachable White Cell",
                "A white cell must be able to reach a white region",
                "edu/rpi/legup/images/nurikabe/contradictions/CantReach.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board;

        NurikabeCell cell = (NurikabeCell) nurikabeBoard.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.WHITE) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }

        ArrayList<Set<NurikabeCell>> regions = NurikabeUtilities.getFloodFillWhite(nurikabeBoard);

        for (Set<NurikabeCell> region : regions) {
            if (region.contains(cell)) {
                return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
            }
        }

        return null;
    }
}
