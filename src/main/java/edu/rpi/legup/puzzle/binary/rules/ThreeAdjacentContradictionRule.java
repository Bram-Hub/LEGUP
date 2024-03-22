package edu.rpi.legup.puzzle.binary.rules;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class ThreeAdjacentContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a zero or one";

    public ThreeAdjacentContradictionRule() {
        super("BINA-CONT-0001",
                "Three Adjacent",
                "There must not be three adjacent zeros or three adjacent ones in a row or column",
                "edu/rpi/legup/images/binary/rules/ThreeAdjacentZerosContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        int height = binaryBoard.getHeight();
        int width = binaryBoard.getWidth();


        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
       

        int cellX = cell.getLocation().x;
        int cellY = cell.getLocation().y;
        System.out.println("X = " + cellX + ", Y = " + cellY);
/*
        if (cell.getType() != BinaryType.ZERO && cell.getType() != BinaryType.ONE) {
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
        if(upTwo.getType() == null)
            upTwo.getType() = UNKNOWN;

        if (cell.getType() == BinaryType.ZERO) {
            if ((upTwo.getType() == BinaryType.ZERO && upOne.getType() == BinaryType.ZERO) ||
                    (leftTwo.getType() == BinaryType.ZERO && leftOne.getType() == BinaryType.ZERO) ||
                    (rightTwo.getType() == BinaryType.ZERO && rightOne.getType() == BinaryType.ZERO) ||
                    (downTwo.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO) ||
                    (leftOne.getType() == BinaryType.ZERO && rightOne.getType() == BinaryType.ZERO) ||
                    (upOne.getType() == BinaryType.ZERO && downOne.getType() == BinaryType.ZERO)) {
                return null;
            }
        }
        else if (cell.getType() == BinaryType.ONE) {
            if ((upTwo.getType() == BinaryType.ONE && upOne.getType() == BinaryType.ONE) ||
                    (leftTwo.getType() == BinaryType.ONE && leftOne.getType() == BinaryType.ONE) ||
                    (rightTwo.getType() == BinaryType.ONE && rightOne.getType() == BinaryType.ONE) ||
                    (downTwo.getType() == BinaryType.ONE && downOne.getType() == BinaryType.ONE) ||
                    (leftOne.getType() == BinaryType.ONE && rightOne.getType() == BinaryType.ONE) ||
                    (upOne.getType() == BinaryType.ONE && downOne.getType() == BinaryType.ONE)) {
                return null;
            }
        }*/

        for (int x = cell.getLocation().x - 2; x >= 0 && x < cell.getLocation().x && x < width - 1; x++) {
            if(binaryBoard.getCell(x, cellY).getType() == binaryBoard.getCell(x + 1, cellY).getType() &&
                binaryBoard.getCell(x + 1, cellY).getType() == binaryBoard.getCell(x + 2, cellY).getType()) {
                return null;
            }
        }

        for (int y = cell.getLocation().y - 2; y >= 0 && y < cell.getLocation().y && y < height - 1; y++) {
            if(binaryBoard.getCell(cellX, y).getType() == binaryBoard.getCell(cellX, y + 1).getType() &&
                binaryBoard.getCell(cellX, y + 1).getType() == binaryBoard.getCell(cellX, y + 2).getType()) {
                return null;
            }
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}