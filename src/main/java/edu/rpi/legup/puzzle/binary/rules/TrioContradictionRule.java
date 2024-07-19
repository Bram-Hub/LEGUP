package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class TrioContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a zero or one";

    public TrioContradictionRule() {
        super(
                "BINA-CONT-0001",
                "Trio",
                "There must not be three adjacent zeros or three adjacent ones in a row or column",
                "edu/rpi/legup/images/binary/rules/TrioContradictionRule.png");
    }

    public BinaryCell[] getCellsXAway(Board board, PuzzleElement puzzleElement, int x) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        int cellX = cell.getLocation().x;
        int cellY = cell.getLocation().y;

        BinaryCell[] cells = new BinaryCell[4]; // [0] up x, [1] down x, [2] forward x, [3] backward x
        cells[0] = null;
        cells[1] = null;
        cells[2] = null;
        cells[3] = null;

        if (binaryBoard.getCell(cellX, cellY + x) != null) {
            cells[0] = binaryBoard.getCell(cellX, cellY + x);
        }
        if (binaryBoard.getCell(cellX, cellY - x) != null) {
            cells[1] = binaryBoard.getCell(cellX, cellY - x);
        }
        if (binaryBoard.getCell(cellX + x, cellY) != null) {
            cells[2] = binaryBoard.getCell(cellX + x, cellY);
        }
        if (binaryBoard.getCell(cellX - x, cellY) != null) {
            cells[3] = binaryBoard.getCell(cellX - x, cellY);
        }

        return cells;
    }
    public boolean checkSurroundPair(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        // [0] up x, [1] down x, [2] forward x, [3] backward x
        BinaryCell[] cellsOneAway = getCellsXAway(board, puzzleElement, 1);
        BinaryCell[] cellsTwoAway = getCellsXAway(board, puzzleElement, 2);

        if (cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            if (cellsOneAway[3] != null
                    && cellsTwoAway[3] != null
                    && cellsOneAway[3].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[3].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[3].getType() == cell.getType()
                        && cellsTwoAway[3].getType() == cell.getType()) {
                    return false;
                }
            }
            if (cellsOneAway[2] != null
                    && cellsTwoAway[2] != null
                    && cellsOneAway[2].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[2].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[2].getType() == cell.getType()
                        && cellsTwoAway[2].getType() == cell.getType()) {
                    return false;
                }
            }
            if (cellsOneAway[1] != null
                    && cellsTwoAway[1] != null
                    && cellsOneAway[1].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[1].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[1].getType() == cell.getType()
                        && cellsTwoAway[1].getType() == cell.getType()) {
                    return false;
                }
            }
            if (cellsOneAway[0] != null
                    && cellsTwoAway[0] != null
                    && cellsOneAway[0].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[0].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[0].getType() == cell.getType()
                        && cellsTwoAway[0].getType() == cell.getType()) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean checkOneTileGap(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        // [0] up x, [1] down x, [2] forward x, [3] backward x
        BinaryCell[] cellsOneAway = getCellsXAway(board, puzzleElement, 1);

        if (cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            if (cellsOneAway[3] != null
                    && cellsOneAway[2] != null
                    && cellsOneAway[3].getType() != BinaryType.UNKNOWN
                    && cellsOneAway[2].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[3].getType() == cell.getType()
                        && cellsOneAway[2].getType() == cell.getType()) {
                    return false;
                }
            }
            if (cellsOneAway[1] != null
                    && cellsOneAway[0] != null
                    && cellsOneAway[1].getType() != BinaryType.UNKNOWN
                    && cellsOneAway[0].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[1].getType() == cell.getType()
                        && cellsOneAway[0].getType() == cell.getType()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {

        boolean surroundPairValid = checkSurroundPair(board, puzzleElement);
        if (!surroundPairValid) {
            return null;
        }
        boolean oneTileGapValid = checkOneTileGap(board, puzzleElement);
        if (!oneTileGapValid) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
