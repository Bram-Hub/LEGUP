package edu.rpi.legup.puzzle.fillapix.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.FillapixUtilities;

import java.util.ArrayList;

public class TooFewBlackCellsContradictionRule extends ContradictionRule {

    public TooFewBlackCellsContradictionRule() {
        super("FPIX-CONT-0001",
                "Too Few Black Cells",
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

        int cellNum = cell.getNumber();
        if (cellNum < 0 || cellNum >= 10) {
            return super.getNoContradictionMessage();
        }
        int numBlack = 0, numEmpty = 0;
        ArrayList<FillapixCell> adjCells = FillapixUtilities.getAdjacentCells(fillapixBoard, cell);
        for (FillapixCell adjCell : adjCells) {
            if (adjCell.getType() == FillapixCellType.BLACK) {
                numBlack++;
            }
            if (adjCell.getType() == FillapixCellType.UNKNOWN) {
                numEmpty++;
            }
        }
        if (numBlack + numEmpty < cellNum) {
            return null;
        }

        return super.getNoContradictionMessage();
    }
}
