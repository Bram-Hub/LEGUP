package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class UnbalancedColumnContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Column must have a value in each cell";

    public UnbalancedColumnContradictionRule() {
        super("BINA-CONT-0004",
                "Unbalanced Column",
                "Each column must contain an equal number of zeros and ones",
                "edu/rpi/legup/images/binary/rules/UnbalancedColumnContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> col = binaryBoard.getCol(cell.getLocation().x);

        int size = col.size();
        int numZeros = 0;
        int numOnes = 0;

        for (BinaryCell item : col) {
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