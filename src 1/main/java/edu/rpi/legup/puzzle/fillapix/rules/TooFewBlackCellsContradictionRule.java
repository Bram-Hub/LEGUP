package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;

import java.awt.*;

public class TooFewBlackCellsContradictionRule extends ContradictionRule {

    public TooFewBlackCellsContradictionRule() {
        super("Too Few Black Cells",
                "There may not be fewer black cells than the number.",
                "edu/rpi/legup/images/fillapix/contradictions/TooFewBlackCells.png");
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
        FillapixBoard fillapixBoard = (FillapixBoard) board;
        FillapixCell cell = (FillapixCell) fillapixBoard.getPuzzleElement(puzzleElement);

        Point loc = cell.getLocation();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                FillapixCell adjCell = fillapixBoard.getCell(loc.x + i, loc.y + j);
                if (adjCell != null) {
                    int cellNum = adjCell.getNumber();
                    if (cellNum >= 0) {
                        int numBlackCells = fillapixBoard.getNumCells(adjCell, FillapixCellType.BLACK);
                        int numUnknownCells = fillapixBoard.getNumCells(adjCell, FillapixCellType.UNKNOWN);
                        if (numBlackCells + numUnknownCells < cellNum) {
                            return null;
                        }
                    }
                }
            }
        }
        return "Board does not contain a contradiction";
    }
}
