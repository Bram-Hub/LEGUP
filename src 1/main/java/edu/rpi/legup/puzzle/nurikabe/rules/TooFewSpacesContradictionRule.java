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

public class TooFewSpacesContradictionRule extends ContradictionRule {

    public TooFewSpacesContradictionRule() {
        super("Too Few Spaces",
                "A region cannot contain less spaces than its number.",
                "edu/rpi/legup/images/nurikabe/contradictions/TooFewSpaces.png");
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
        if (cell.getType() != NurikabeType.WHITE && cell.getType() != NurikabeType.NUMBER) {
            return "Contradiction must be a white or a numbered cell";
        }

        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getPossibleWhiteRegions(nurikabeBoard);
        Set<NurikabeCell> whiteRegion = regions.getSet(cell);
        NurikabeCell numberedCell = null;
        for (NurikabeCell c : whiteRegion) {
            if (c.getType() == NurikabeType.NUMBER) {
                numberedCell = c;
                break;
            }
        }

        if (numberedCell != null && whiteRegion.size() < numberedCell.getData()) {
            System.err.println("Cell Value: " + numberedCell.getData() + ", Loc: " + cell.getLocation() + ", region: " + whiteRegion.size());
            return null;
        }
        return "Does not contain a contradiction at this index";
    }
}
