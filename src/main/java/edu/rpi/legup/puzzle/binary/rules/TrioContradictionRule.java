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

    /**
     * This method checks the surrounding cells of a given puzzle element at a specified distance in
     * both the vertical and horizontal directions
     *
     * @param board The board where the puzzle elements are located
     * @param puzzleElement The puzzle element from which the distance is calculated
     * @param n The distance away from the puzzle element to retrieve the surrounding cells
     * @return An array of BinaryCells representing the surrounding cells
     */
    public BinaryCell[] getCellsNAway(Board board, PuzzleElement puzzleElement, int n) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        int cellX = cell.getLocation().x;
        int cellY = cell.getLocation().y;

        BinaryCell[] cells = new BinaryCell[4]; // [0] up x, [1] down x, [2] right x, [3] left x
        cells[0] = null;
        cells[1] = null;
        cells[2] = null;
        cells[3] = null;

        if (binaryBoard.getCell(cellX, cellY + n) != null) {
            cells[0] = binaryBoard.getCell(cellX, cellY + n);
        }
        if (binaryBoard.getCell(cellX, cellY - n) != null) {
            cells[1] = binaryBoard.getCell(cellX, cellY - n);
        }
        if (binaryBoard.getCell(cellX + n, cellY) != null) {
            cells[2] = binaryBoard.getCell(cellX + n, cellY);
        }
        if (binaryBoard.getCell(cellX - n, cellY) != null) {
            cells[3] = binaryBoard.getCell(cellX - n, cellY);
        }

        return cells;
    }

    /**
     * Checks whether the cell and its two surrounding cells form a trio of zeros or ones; If a trio
     * is found, it indicates a contradiction
     *
     * @param board The board where the puzzle elements are located
     * @param puzzleElement The puzzle element to check for contradiction
     * @return true if no contradiction is found, false if contradiction detected
     */
    public boolean checkSurroundPair(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        // [0] up n, [1] down n, [2] right n, [3] left n
        BinaryCell[] cellsOneAway = getCellsNAway(board, puzzleElement, 1);
        BinaryCell[] cellsTwoAway = getCellsNAway(board, puzzleElement, 2);

        if (cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            // left one and left two
            if (cellsOneAway[3] != null
                    && cellsTwoAway[3] != null
                    && cellsOneAway[3].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[3].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[3].getType() == cell.getType()
                        && cellsTwoAway[3].getType() == cell.getType()) {
                    return false;
                }
            }
            // right one and right two
            if (cellsOneAway[2] != null
                    && cellsTwoAway[2] != null
                    && cellsOneAway[2].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[2].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[2].getType() == cell.getType()
                        && cellsTwoAway[2].getType() == cell.getType()) {
                    return false;
                }
            }
            // down one and down two
            if (cellsOneAway[1] != null
                    && cellsTwoAway[1] != null
                    && cellsOneAway[1].getType() != BinaryType.UNKNOWN
                    && cellsTwoAway[1].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[1].getType() == cell.getType()
                        && cellsTwoAway[1].getType() == cell.getType()) {
                    return false;
                }
            }
            // up one and up two
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

    /**
     * Checks whether there are two of the same cell type separated by one cell that also has the
     * same type in any direction. If a trio is found, it indicates a contradiction
     *
     * @param board The board where the puzzle elements are located
     * @param puzzleElement The puzzle element to check for contradiction
     * @return true if no contradiction is found, false if contradiction detected
     */
    public boolean checkOneTileGap(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        // [0] up n, [1] down n, [2] right n, [3] left n
        BinaryCell[] cellsOneAway = getCellsNAway(board, puzzleElement, 1);

        if (cell.getType() == BinaryType.ONE || cell.getType() == BinaryType.ZERO) {
            // left one and right one
            if (cellsOneAway[3] != null
                    && cellsOneAway[2] != null
                    && cellsOneAway[3].getType() != BinaryType.UNKNOWN
                    && cellsOneAway[2].getType() != BinaryType.UNKNOWN) {
                if (cellsOneAway[3].getType() == cell.getType()
                        && cellsOneAway[2].getType() == cell.getType()) {
                    return false;
                }
            }
            // down one and up one
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

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
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
