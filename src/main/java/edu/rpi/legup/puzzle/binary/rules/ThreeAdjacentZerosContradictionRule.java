package edu.rpi.legup.puzzle.binary.rules;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import java.util.Set;
public class ThreeAdjacentZerosContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a zero";

    public ThreeAdjacentZerosContradictionRule() {
        super("BINA-CONT-0001",
                "Three Adjacent Zeros",
                "There must not be three adjacent zeros in a row or column",
                "currentlynoimage.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        if (cell.getType() != BinaryType.ZERO) {
            return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
        }

        BinaryCell upTwo = binaryBoard.getCell(cell.getLocation().x, cell.getLocation().y+2);
        BinaryCell upOne = binaryBoard.getCell(cell.getLocation().x, cell.getLocation().y+1);
        BinaryCell leftTwo = binaryBoard.getCell(cell.getLocation().x-2, cell.getLocation().y);
        BinaryCell leftOne = binaryBoard.getCell(cell.getLocation().x-1, cell.getLocation().y);
        BinaryCell rightTwo = binaryBoard.getCell(cell.getLocation().x+2, cell.getLocation().y);
        BinaryCell rightOne = binaryBoard.getCell(cell.getLocation().x+1, cell.getLocation().y);
        BinaryCell downTwo = binaryBoard.getCell(cell.getLocation().x, cell.getLocation().y-2);
        BinaryCell downOne = binaryBoard.getCell(cell.getLocation().x, cell.getLocation().y-1);

        if ((upTwo.getType() == BinaryType.ZERO && upOne.getType() == BinaryType.ZERO) ||
                (leftTwo.getType() == BinaryType.ZERO && leftOne.getType() == BinaryType.ZERO) ||
                (rightTwo.getType() == BinaryType.ZERO && rightOne.getType() == BinaryType.ZERO) ||
                (downTwo.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO) ||
                (leftOne.getType() == BinaryType.ZERO && rightOne.getType() == BinaryType.ZERO) ||
                (upOne.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO)) {
            return null;
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
