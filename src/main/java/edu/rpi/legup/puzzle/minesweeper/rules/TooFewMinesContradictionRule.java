package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.minesweeper.*;
import java.util.ArrayList;

public class TooFewMinesContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a region";

    public TooFewMinesContradictionRule() {
        super(
                "MINE-CONT-0000",
                "Too Few Mines",
                "A number cell can not have less than it's number of mines around it\n",
                "edu/rpi/legup/images/minesweeper/contradictions/TooFewMines.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        MinesweeperBoard minesweeperBoard = (MinesweeperBoard) board;
        MinesweeperCell cell = (MinesweeperCell) minesweeperBoard.getPuzzleElement(puzzleElement);

        int cellNum = cell.getTileNumber();
        if (cellNum <= 0 || cellNum >= 9) {
            return super.getNoContradictionMessage();
        }
        int numMines = 0;
        int numUnset = 0;
        ArrayList<MinesweeperCell> adjCells =
                MinesweeperUtilities.getAdjacentCells(minesweeperBoard, cell);
        for (MinesweeperCell adjCell : adjCells) {
            if(adjCell.getTileType() == MinesweeperTileType.MINE) {
                numMines++;
            }
            if(adjCell.getTileType() == MinesweeperTileType.UNSET) {
                numUnset++;
            }
        }

        if (cellNum > numUnset + numMines) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
