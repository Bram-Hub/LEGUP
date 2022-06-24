package edu.rpi.legup.puzzle.battleship.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.battleship.BattleshipBoard;
import edu.rpi.legup.puzzle.battleship.BattleshipCell;
import edu.rpi.legup.puzzle.battleship.BattleshipType;

import java.util.List;

public class AdjacentShipsContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE
            = "No instance of the contradiction " + this.ruleName + " here";

    public AdjacentShipsContradictionRule() {
        super("BTSP-CONT-0001",
                "Adjacent Ships",
                "Cells next to the battleship must be water.",
                "edu/rpi/legup/images/battleship/contradictions" +
                        "/AdjacentShips.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific
     * {@link PuzzleElement} index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent {@link PuzzleElement}
     * @return              <code>null</code> if the transition contains a
     *                      contradiction at the specified {@link PuzzleElement},
     *                      otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BattleshipBoard bsBoard = (BattleshipBoard) board;
        BattleshipCell cell = (BattleshipCell) bsBoard.getPuzzleElement(puzzleElement);

        // check orthogonally adjacent cells
        List<BattleshipCell> orthoAdjCells
                = bsBoard.getAdjacent(cell);

        BattleshipCell up = orthoAdjCells.get(0);

        return null;
    }
}
