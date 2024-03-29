package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileType;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperUtilities;
import java.util.ArrayList;

public class MoreBombsThanFlagContradictionRule extends ContradictionRule {

    public MoreBombsThanFlagContradictionRule() {
        super(
                "MINE-CONT-0001",
                "More Bombs Than Flag",
                "There can not be more Bombs around a flag than the specified number\n",
                "edu/rpi/legup/images/minesweeper/contradictions/Bomb_Surplus.jpg");
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

        int cellNum = cell.getTileNumber();
        if (cellNum < 0 || cellNum >= 10) {
            return super.getNoContradictionMessage();
        }
        int numBlack = 0;
        ArrayList<MinesweeperCell> adjCells = MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if (adjCell.getTileType() == MinesweeperTileType.BOMB) {
                numBlack++;
            }
        }
        if (numBlack > cellNum) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
