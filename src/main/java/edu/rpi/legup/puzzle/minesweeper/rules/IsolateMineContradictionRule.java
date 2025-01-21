package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileType;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperUtilities;
import java.util.ArrayList;

public class IsolateMineContradictionRule extends ContradictionRule {

    public IsolateMineContradictionRule() {
        super(
                "MINE-CONT-0002",
                "Isolate Mine",
                "A mine cell must see a number cell",
                "edu/rpi/legup/images/minesweeper/contradictions/IsolateMine.png");
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
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board;
        MinesweeperCell cell = (MinesweeperCell) minesweeperBoard.getPuzzleElement(puzzleElement);

        if (cell.getTileNumber() != -1) {
            return super.getNoContradictionMessage();
        }
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.NUMBER) {
                return super.getNoContradictionMessage();
            }
        }
        return null;
    }
}
