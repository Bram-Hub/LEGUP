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

public class IsolateBlackContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Contradiction applied incorrectly. No isolated Blacks.";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a black cell";

    public IsolateBlackContradictionRule() {
        super("NURI-CONT-0003",
                "Isolated Black",
                "There must still be a possibility to connect every Black cell",
                "edu/rpi/legup/images/nurikabe/contradictions/BlackArea.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific
     * {@link PuzzleElement} index using this rule.
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return <code>null</code> if the transition contains a
     * contradiction at the specified {@link PuzzleElement},
     * otherwise return a no contradiction message.
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board;
        NurikabeCell cell = (NurikabeCell) nurikabeBoard.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.BLACK) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }

        DisjointSets<NurikabeCell> blackRegions = NurikabeUtilities.getPossibleBlackRegions(nurikabeBoard);
        boolean oneRegion = false;
        for (Set<NurikabeCell> region : blackRegions.getAllSets()) {
            for (NurikabeCell c : region) {
                if (c.getType() == NurikabeType.BLACK) {
                    if (oneRegion) {
                        return null;
                    }
                    else {
                        oneRegion = true;
                        break;
                    }
                }
            }
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
