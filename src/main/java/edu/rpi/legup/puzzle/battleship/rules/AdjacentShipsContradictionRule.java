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
     * {@link PuzzleElement} index using this rule.
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent {@link PuzzleElement}
     * @return <code>null</code> if the transition contains a
     * contradiction at the specified {@link PuzzleElement},
     * otherwise return a no contradiction message.
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BattleshipBoard bsBoard = (BattleshipBoard) board;
        BattleshipCell cell = (BattleshipCell) bsBoard.getPuzzleElement(puzzleElement);

        // rule only applies to battleship cells
        if (!BattleshipType.isShip(cell.getType())) {
            return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
        }

        // check orthogonally adjacent cells
        List<BattleshipCell> orthoAdjCells
                = bsBoard.getAdjOrthogonals(cell);

        BattleshipCell up = orthoAdjCells.get(0);
        BattleshipCell right = orthoAdjCells.get(1);
        BattleshipCell down = orthoAdjCells.get(2);
        BattleshipCell left = orthoAdjCells.get(3);

        boolean isVertical = (up != null && BattleshipType.isShip(up.getData()))
                || (down != null && BattleshipType.isShip(down.getData()));

        boolean isHorizontal = (left != null && BattleshipType.isShip(left.getData()))
                || (right != null && BattleshipType.isShip(right.getData()));

        // ships cannot be both vertical and horizontal
        if (isVertical && isHorizontal) {
            return null;
        }

        // check diagonally adjacent cells
        List<BattleshipCell> diagAdjCells
                = bsBoard.getAdjDiagonals(cell);

        BattleshipCell upRight = diagAdjCells.get(0);
        BattleshipCell downRight = diagAdjCells.get(1);
        BattleshipCell downLeft = diagAdjCells.get(2);
        BattleshipCell upLeft = diagAdjCells.get(3);

        // diagonally adjacent cells must be water
        if (upRight != null && BattleshipType.isShip(upRight.getData())) {
            return null;
        }
        if (downRight != null && BattleshipType.isShip(downRight.getData())) {
            return null;
        }
        if (downLeft != null && BattleshipType.isShip(downLeft.getData())) {
            return null;
        }
        if (upLeft != null && BattleshipType.isShip(upLeft.getData())) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
