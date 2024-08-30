package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.ArrayList;

public class WastedBlockerContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";

    public WastedBlockerContradictionRule() {
        super(
                "BINA-CONT-0004",
                "Wasted Blocker",
                "There exists a cell in this row/column that allocates a digit unnecessarily and" +
                        " will cause a future trio to appear",
                "edu/rpi/legup/images/binary/rules/WastedBlockerContradictionRule.png");
    }

    /*
     i [ n ] j
     i -> digit on left (0 if no digit exists)
     n -> number of empty cells
     j -> digit on right (0 if no digit exists)
     neededZeros = ( n + i + j ) / 3
    */
    /**
     * Calculates the number of zeros needed in a sequence based on the values on either side and the number of empty cells.
     *
     * @param leftVal The value on the left side of the empty cells
     * @param rightVal The value on the right side of the empty cells
     * @param emptyCellsInCurSec The number of empty cells in the current section
     * @return The number of zeros needed in the sequence
     */
    private int calculateNeededZeros(int leftVal, int rightVal, int emptyCellsInCurSec) {
        int leftCopy = leftVal;
        int rightCopy = rightVal;
        if (leftCopy == -1) {
            leftCopy = 0;
        }
        if (rightCopy == -1) {
            rightCopy = 0;
        }
        return ((emptyCellsInCurSec + leftCopy + rightCopy) / 3);
    }

    /*
     i [ n ] j
     i -> digit on left (1 if no digit exists)
     n -> number of empty cells
     j -> digit on right (1 if no digit exists)
     neededOnes = ( n + ( 1 - i ) + ( 1 - j ) ) / 3
    */
    /**
     * Calculates the number of ones needed in a sequence based on the values on either side and the number of empty cells
     *
     * @param leftVal The value on the left side of the empty cells
     * @param rightVal The value on the right side of the empty cells
     * @param emptyCellsInCurSec The number of empty cells in the current section
     * @return The number of ones needed in the sequence
     */
    private int calculateNeededOnes(int leftVal, int rightVal, int emptyCellsInCurSec) {
        int leftCopy = leftVal;
        int rightCopy = rightVal;
        if (leftCopy == -1) {
            leftCopy = 1;
        }
        if (rightCopy == -1) {
            rightCopy = 1;
        }
        return ((emptyCellsInCurSec + (1 - leftCopy) + (1 - rightCopy)) / 3);
    }

    /**
     * Checks a sequence (row or column) to see if a wasted blocker digit is used
     *
     * @param seq The sequence to check
     * @return Null if the sequence contains a contradiction, otherwise an error message
     */
    private String checkSequence(ArrayList<BinaryType> seq) {
        int numZeros = 0;
        int numOnes = 0;
        boolean emptyCell = false;
        int emptyCellsInCurSec = 0;
        int neededZeros = 0;
        int neededOnes = 0;

        for (int i = 0; i < seq.size(); i++) {
            if (seq.get(i).equals(BinaryType.ZERO) || seq.get(i).equals(BinaryType.ONE)) {
                if (seq.get(i).equals(BinaryType.ZERO)) {
                    numZeros++;
                } else if (seq.get(i).equals(BinaryType.ONE)) {
                    numOnes++;
                }

                if (emptyCell) {
                    if (emptyCellsInCurSec > 1) { // Ignore case where there is only one empty cell
                        int leftVal;
                        int rightVal;
                        // Check if left cell is out of bounds
                        if (i-emptyCellsInCurSec-1 < 0) {
                            leftVal = -1;
                        } else {
                            leftVal = seq.get(i-emptyCellsInCurSec-1).toValue();
                        }
                        rightVal = seq.get(i).toValue();
                        neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                        neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
                    }
                    emptyCell = false;
                    emptyCellsInCurSec = 0;
                }
            } else {
                if (!emptyCell) {
                    emptyCell = true;
                }
                emptyCellsInCurSec++;
            }
        }

        // Check last cell is empty
        if (emptyCell) {
            if (emptyCellsInCurSec > 1) { // Ignore case where there is only one empty cell
                int leftVal;
                int rightVal;
                // Check if left cell is out of bounds
                if (seq.size()-1-emptyCellsInCurSec-1 < 0) {
                    leftVal = -1;
                } else {
                    leftVal = seq.get(seq.size()-1-emptyCellsInCurSec).toValue();
                }
                rightVal = -1;
                neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
            }
        }

        // Check if the number of needed zeros or ones exceeds half the sequence length
        // If so, return null to indicate contradiction has occurred
        if ((numZeros + neededZeros > seq.size()/2) || (numOnes + neededOnes > seq.size()/2)) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
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

        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        ArrayList<BinaryType> row = binaryBoard.getRowTypes(cell.getLocation().y);
        if (checkSequence(row) == null) {
            return null;
        }

        ArrayList<BinaryType> col = binaryBoard.getColTypes(cell.getLocation().x);
        if (checkSequence(col) == null) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
