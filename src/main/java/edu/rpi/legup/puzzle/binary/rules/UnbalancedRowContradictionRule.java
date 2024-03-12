package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class UnbalancedRowContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row must have a value in each cell";

    public UnbalancedRowContradictionRule() {
        super("BINA-CONT-0003",
                "Unbalanced Row",
                "Each row must contain an equal number of zeros and ones",
                "edu/rpi/legup/images/binary/rules/UnbalancedRowContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> row = binaryBoard.getRow(cell.getLocation().y);

        int size = row.size();
        int numZeros = 0;
        int numOnes = 0;

        for (BinaryCell item : row) {
            if (item.getType() == BinaryType.ZERO) {
                numZeros++;
            }
            else if(item.getType() == BinaryType.ONE) {
                numOnes++;
            }
        }
        if (numZeros + numOnes != size) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }
        if (numZeros != numOnes) {
            return null;
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}