package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.*;
import java.util.ArrayList;

public class LessBombsThanFlagContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a region";

    public LessBombsThanFlagContradictionRule() {
        super(
                "MINE-CONT-0000",
                "Less Bombs Than Flag",
                "There can not be less then the number of Bombs around a flag then the specified number\n",
                "edu/rpi/legup/images/nurikabe/contradictions/NoNumber.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board;
        MinesweeperCell cell = (MinesweeperCell) minesweeperBoard.getPuzzleElement(puzzleElement);

        int cellNum = cell.getTileNumber();
        if (cellNum <= 0 || cellNum >= 9) {
            return super.getNoContradictionMessage();
        }
        int numBombs = 0;
        int numUnset = 0;
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if(adjCell.getTileType() == MinesweeperTileType.BOMB) {
                numBombs++;
            }
            if(adjCell.getTileType() == MinesweeperTileType.UNSET) {
                numUnset++;
            }
        }

        if (cellNum > numUnset + numBombs) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
