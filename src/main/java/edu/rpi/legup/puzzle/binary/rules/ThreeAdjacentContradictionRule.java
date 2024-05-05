package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class ThreeAdjacentContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a zero or one";

    public ThreeAdjacentContradictionRule() {
        super(
                "BINA-CONT-0001",
                "Three Adjacent",
                "There must not be three adjacent zeros or three adjacent ones in a row or column",
                "edu/rpi/legup/images/binary/rules/ThreeAdjacentContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        int height = binaryBoard.getHeight();
        int width = binaryBoard.getWidth();

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        System.out.println("THE CELL IS : " + cell.getType());
        int cellX = cell.getLocation().x;
        int cellY = cell.getLocation().y;
        BinaryCell oneUp = null;
        BinaryCell oneDown = null;
        BinaryCell oneForward = null;
        BinaryCell oneBackward = null;
        BinaryCell twoUp = null;
        BinaryCell twoDown = null;
        BinaryCell twoForward = null;
        BinaryCell twoBackward = null;
        if (binaryBoard.getCell(cellX, cellY + 1) != null) {
            oneUp = binaryBoard.getCell(cellX, cellY + 1);
        }
        if (binaryBoard.getCell(cellX, cellY - 1) != null) {
            oneDown = binaryBoard.getCell(cellX, cellY - 1);
        }
        if (binaryBoard.getCell(cellX + 1, cellY) != null) {
            oneForward = binaryBoard.getCell(cellX + 1, cellY);
        }
        if (binaryBoard.getCell(cellX - 1, cellY) != null) {
            oneBackward = binaryBoard.getCell(cellX - 1, cellY);
        }
        if (binaryBoard.getCell(cellX, cellY + 2) != null) {
            twoUp = binaryBoard.getCell(cellX, cellY + 2);
        }
        if (binaryBoard.getCell(cellX, cellY - 2) != null) {
            twoDown = binaryBoard.getCell(cellX, cellY - 2);
        }
        if (binaryBoard.getCell(cellX + 2, cellY) != null) {
            twoForward = binaryBoard.getCell(cellX + 2, cellY);
        }
        if (binaryBoard.getCell(cellX - 2, cellY) != null) {
            twoBackward = binaryBoard.getCell(cellX - 2, cellY);
        }

        if (cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            if (twoBackward != null
                    && oneBackward != null
                    && twoBackward.getType() != BinaryType.UNKNOWN
                    && oneBackward.getType() != BinaryType.UNKNOWN) {
                if (twoBackward.getType() == cell.getType()
                        && oneBackward.getType() == cell.getType()) {
                    System.out.println("1");
                    return null;
                }
            }
            if (twoForward != null
                    && oneForward != null
                    && twoForward.getType() != BinaryType.UNKNOWN
                    && oneForward.getType() != BinaryType.UNKNOWN) {
                if (twoForward.getType() == cell.getType()
                        && oneForward.getType() == cell.getType()) {
                    System.out.println("2");
                    return null;
                }
            }
            if (twoDown != null
                    && oneDown != null
                    && twoDown.getType() != BinaryType.UNKNOWN
                    && oneDown.getType() != BinaryType.UNKNOWN) {
                if (twoDown.getType() == cell.getType() && oneDown.getType() == cell.getType()) {
                    System.out.println("3");
                    return null;
                }
            }
            if (twoUp != null
                    && oneUp != null
                    && twoUp.getType() != BinaryType.UNKNOWN
                    && oneUp.getType() != BinaryType.UNKNOWN) {
                if (twoUp.getType() == cell.getType() && oneUp.getType() == cell.getType()) {
                    System.out.println("4");
                    return null;
                }
            }
            if (oneBackward != null
                    && oneForward != null
                    && oneBackward.getType() != BinaryType.UNKNOWN
                    && oneForward.getType() != BinaryType.UNKNOWN) {
                if (oneBackward.getType() == cell.getType()
                        && oneForward.getType() == cell.getType()) {
                    System.out.println("5");
                    return null;
                }
            }
            if (oneUp != null
                    && oneDown != null
                    && oneUp.getType() != BinaryType.UNKNOWN
                    && oneDown.getType() != BinaryType.UNKNOWN) {
                if (oneUp.getType() == cell.getType() && oneDown.getType() == cell.getType()) {
                    System.out.println("6");
                    return null;
                }
            }
        }
        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
