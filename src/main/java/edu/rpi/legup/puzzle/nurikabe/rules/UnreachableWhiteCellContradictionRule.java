package edu.rpi.legup.puzzle.nurikabe.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.NurikabeUtilities;
import java.awt.*;
import java.util.*;

public class UnreachableWhiteCellContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Cell at this index can be reached";
    private final String INVALID_USE_MESSAGE = "Does not contain a contradiction at this index";

    public UnreachableWhiteCellContradictionRule() {
        super(
                "NURI-CONT-0002",
                "Unreachable White Cell",
                "A white cell must be able to reach a white region",
                "edu/rpi/legup/images/nurikabe/contradictions/CantReach.png");
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
        NurikabeBoard nurikabeBoard = (NurikabeBoard) board;

        NurikabeCell cell = (NurikabeCell) nurikabeBoard.getPuzzleElement(puzzleElement);
        if (cell.getType() != NurikabeType.WHITE) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }

        int height = nurikabeBoard.getHeight();
        int width = nurikabeBoard.getWidth();

        // Get regions
        HashMap<NurikabeCell, Integer> whiteRegionMap =
                NurikabeUtilities.getWhiteRegionMap(nurikabeBoard);
//        if (whiteRegionMap.containsKey(cell)) {
//            return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
//        }

        // BFS to a region

        // Create a queue for BFS
        LinkedList<NurikabeCell> queue = new LinkedList<>();

        // Mark the current node as visited and enqueue it
        HashMap<NurikabeCell, Boolean> visited = new HashMap<>();
        visited.put(cell, true);
        queue.add(cell);
        int pathLength = 1;
        while (queue.size() != 0) {
            // Set of adjacent squares
            Set<NurikabeCell> adj = new HashSet<>();
            while (queue.size() != 0) {
                // Dequeue a vertex from queue and print it
                NurikabeCell s = queue.poll();

                Point loc = s.getLocation();
                // First check if the side is on the board
                if (loc.x >= 1) {
                    adj.add(nurikabeBoard.getCell(loc.x - 1, loc.y));
                }
                if (loc.x < width - 1) {
                    adj.add(nurikabeBoard.getCell(loc.x + 1, loc.y));
                }
                if (loc.y >= 1) {
                    adj.add(nurikabeBoard.getCell(loc.x, loc.y - 1));
                }
                if (loc.y < height - 1) {
                    adj.add(nurikabeBoard.getCell(loc.x, loc.y + 1));
                }

                for (NurikabeCell n : adj) {
                    int regionNeed = whiteRegionMap.getOrDefault(n, -1);
                    if (pathLength <= regionNeed || (regionNeed == 0 && pathLength == 1)) {
                        return super.getNoContradictionMessage()
                                + ": "
                                + this.NO_CONTRADICTION_MESSAGE;
                    }
                }
            }

            for (NurikabeCell n : adj) {
                if (!visited.getOrDefault(n, false)
                        && (n.getType() == NurikabeType.UNKNOWN
                                || n.getType() == NurikabeType.WHITE)) {
                    visited.put(n, true);
                    queue.add(n);
                }
            }
            ++pathLength;
        }

        return null;
    }
}
