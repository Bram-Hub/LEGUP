package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class UnbalancedRowOrColumnContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row or column must have a value in each cell";

    public UnbalancedRowOrColumnContradictionRule() {
        super("BINA-CONT-0002",
                "Unbalanced Row Or Column",
                "Each row or column must contain an equal number of zeros and ones",
                "edu/rpi/legup/images/binary/rules/UnbalancedRowContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> row = binaryBoard.getRow(cell.getLocation().y);

        int size = row.size();
        int rowNumZeros = 0;
        int rowNumOnes = 0;

        for (BinaryCell item : row) {
            if (item.getType() == BinaryType.ZERO) {
                rowNumZeros++;
            }
            else if(item.getType() == BinaryType.ONE) {
                rowNumOnes++;
            }
        }
        if (rowNumZeros + rowNumOnes != size) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }
        if (rowNumZeros != rowNumOnes) {
            return null;
        }

        Set<BinaryCell> col = binaryBoard.getCol(cell.getLocation().x);

        size = col.size();
        int colNumZeros = 0;
        int colNumOnes = 0;

        for (BinaryCell item : col) {
            if (item.getType() == BinaryType.ZERO) {
                colNumZeros++;
            }
            else if(item.getType() == BinaryType.ONE) {
                colNumOnes++;
            }
        }
        if (colNumZeros + colNumOnes != size) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }
        if (colNumZeros != colNumOnes) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}