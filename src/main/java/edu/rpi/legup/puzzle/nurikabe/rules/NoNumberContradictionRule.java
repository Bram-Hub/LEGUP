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

public class NoNumberContradictionRule extends ContradictionRule {

    public NoNumberContradictionRule() {
        super("No Number",
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
            return "Contradiction must be a white cell";
        }
        DisjointSets<NurikabeCell> regions = NurikabeUtilities.getNurikabeRegions(nurikabeBoard);
        Set<NurikabeCell> whiteRegion = regions.getSet(cell);
        for (NurikabeCell c : whiteRegion) {
            if (c.getType() == NurikabeType.NUMBER) {
                return "Does not contain a contradiction at this index";
            }
        }
        for (NurikabeCell c : whiteRegion) {
            // System.out.println(c.getLocation().x + "\t" + c.getLocation().y);
            NurikabeCell top = nurikabeBoard.getCell(c.getLocation().x, c.getLocation().y+1);
            NurikabeCell left = nurikabeBoard.getCell(c.getLocation().x-1, c.getLocation().y);
            NurikabeCell right = nurikabeBoard.getCell(c.getLocation().x+1, c.getLocation().y);
            NurikabeCell bottom = nurikabeBoard.getCell(c.getLocation().x, c.getLocation().y-1);

            if (isEmptyCell(top) || isEmptyCell(left) || isEmptyCell(right) || isEmptyCell(bottom))
                return "Must be surrounded by black cells";
        }
        return null;
    }

    /**
     * Checks whether a give NurikabeCell is empty.
     *
     * @param cell  NurikabeCell to check if empty
     * @return      false if the NurikabeCell is not empty or null, true otherwise
     */
    private boolean isEmptyCell(NurikabeCell cell)
    {
        if (cell == null)
            return false;
        NurikabeType cellType = cell.getType();
        return cellType != NurikabeType.BLACK && cellType != NurikabeType.WHITE;
    }
}
