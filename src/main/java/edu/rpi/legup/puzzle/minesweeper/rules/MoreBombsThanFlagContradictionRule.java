package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileType;
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
        if (cellNum <= 0 || cellNum >= 9) {
            return super.getNoContradictionMessage();
        }
        int numEmpty = 0;
        int numAdj = 0;
        int numBombs = 0;
        int numUnset = 0;
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            numAdj++;
            if (adjCell.getTileType() == MinesweeperTileType.EMPTY) {
                numEmpty++;
                System.out.println("loc " + adjCell.getLocation().x + ", " + adjCell.getLocation().y);
            }
            if(adjCell.getTileType() == MinesweeperTileType.BOMB) {
                numBombs++;
            }
            if(adjCell.getTileType() == MinesweeperTileType.UNSET) {
                numUnset++;
            }
        }
        System.out.println("num empty " + numEmpty);
        System.out.println("num adj " + numAdj);
        System.out.println("cell num " + cellNum);
        System.out.println("num bombs " + numBombs);
        System.out.println("num unset " + numUnset);

        if (cellNum < numBombs) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
