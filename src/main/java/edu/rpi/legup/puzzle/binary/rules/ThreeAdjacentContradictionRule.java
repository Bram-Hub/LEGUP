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

        if(cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            for (int x = cell.getLocation().x - 2; x >= 0 && x < cell.getLocation().x && x < width - 2; x++) {

                if(binaryBoard.getCell(x, cellY).getType() == binaryBoard.getCell(x + 1, cellY).getType() &&
                    binaryBoard.getCell(x + 1, cellY).getType() == binaryBoard.getCell(x + 2, cellY).getType()) {
                    System.out.println("CUR XY fail X= " + cellX + " , " + cellY);
                    return null;
                }
            }

            for (int y = cell.getLocation().y - 2; y >= 0 && y < cell.getLocation().y && y < height - 2; y++) {

                if(binaryBoard.getCell(cellX, y).getType() == binaryBoard.getCell(cellX, y + 1).getType() &&
                    binaryBoard.getCell(cellX, y + 1).getType() == binaryBoard.getCell(cellX, y + 2).getType()) {
                    System.out.println("CUR XY fail Y= " + cellX + " , " + cellY);
                    return null;
                }
            }
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}