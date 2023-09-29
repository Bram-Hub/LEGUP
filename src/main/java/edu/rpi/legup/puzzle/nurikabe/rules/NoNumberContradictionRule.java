package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import edu.rpi.legup.utility.DisjointSets;

import java.util.Set;
import java.util.List;

public class NoNumberContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a white cell";
    private final String NOT_SURROUNDED_BY_BLACK_MESSAGE = "Must be surrounded by black cells";

    public NoNumberContradictionRule() {
        super("NURI-CONT-0005",
                "No Number",
                "All enclosed white regions must have a number.",
                "edu/rpi/legup/images/nurikabe/contradictions/NoNumber.png");
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

        Set<NurikabeCell> region = NurikabeUtilities.getSurroundedRegionOf(nurikabeBoard, cell);

        boolean numberExists = false;
        for (NurikabeCell c : region) {
            if (c.getType() == NurikabeType.NUMBER) {
                numberExists = true;
                break;
            }
        }
        if (!numberExists) {
            return null;
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }

    /**
     * Checks whether a give NurikabeCell is empty.
     *
     * @param cell NurikabeCell to check if empty
     * @return false if the NurikabeCell is not empty or null, true otherwise
     */
    private boolean isEmptyCell(NurikabeCell cell) {
        if (cell == null) {
            return false;
        }
        NurikabeType cellType = cell.getType();
        return cellType != NurikabeType.BLACK && cellType != NurikabeType.WHITE;
    }
}
