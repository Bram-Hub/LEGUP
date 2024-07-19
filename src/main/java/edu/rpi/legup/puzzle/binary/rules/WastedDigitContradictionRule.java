package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.ArrayList;

public class WastedDigitContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";

    public WastedDigitContradictionRule() {
        super(
                "BINA-CONT-0004",
                "Wasted Digit",
                "There exists a cell in this row/column that allocates a digit unnecessarily and will cause a future trio to appear",
                "edu/rpi/legup/images/binary/rules/WastedDigitContradictionRule.png");
    }


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

    private int convertTypeToInt(BinaryType type) {
        if (type.equals(BinaryType.ZERO)) {
            return 0;
        } else if (type.equals(BinaryType.ONE)) {
            return 1;
        } else {
            return -1;
        }
    }

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
                    if (emptyCellsInCurSec > 1) {
                        if (i-emptyCellsInCurSec-1 < 0) {
                            int leftVal = -1;
                            int rightVal = convertTypeToInt(seq.get(i));
                            neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                            neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NEEDED ZEROS: " + calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NEEDED ONES: " + calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NUM EMPTY BEFORE THIS : " + emptyCellsInCurSec + ", surrounded by " + leftVal + " and " + rightVal);
                        } else {
                            int leftVal = convertTypeToInt(seq.get(i-emptyCellsInCurSec-1));
                            int rightVal = convertTypeToInt(seq.get(i));
                            neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                            neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NEEDED ZEROS: " + calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NEEDED ONES: " + calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                            System.out.println("NUM EMPTY BEFORE THIS : " + emptyCellsInCurSec + ", surrounded by " + leftVal + " and " + rightVal);
                        }
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
            //System.out.println(seq.get(i).toString());
        }

        // check if empty cells surrounded by out of bounds
        if (emptyCell) {
            if (emptyCellsInCurSec > 1) {
                if (seq.size()-1-emptyCellsInCurSec-1 < 0) {
                    int leftVal = -1;
                    int rightVal = -1;
                    neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                    neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NEEDED ZEROS: " + calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NEEDED ONES: " + calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NUM EMPTY BEFORE THIS : " + emptyCellsInCurSec+ ", surrounded by " + leftVal + " and " + rightVal);
                } else {
                    int leftVal = convertTypeToInt(seq.get(seq.size()-1-emptyCellsInCurSec));
                    int rightVal = -1;
                    neededZeros += calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
                    neededOnes += calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NEEDED ZEROS: " + calculateNeededZeros(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NEEDED ONES: " + calculateNeededOnes(leftVal, rightVal, emptyCellsInCurSec);
//                    System.out.println("NUM EMPTY BEFORE THIS : " + emptyCellsInCurSec+ ", surrounded by " + leftVal + " and " + rightVal);
                }
            }
            emptyCell = false;
            emptyCellsInCurSec = 0;
        }

//        if (numZeros + neededZeros > seq.size()/2) {
//            System.out.println("NEED TOO MANY ZEROS");
//            return null;
//        }
//
//        if (numOnes + neededOnes > seq.size()/2) {
//            System.out.println("NEED TOO MANY ONES");
//            return null;
//        }

        if ((numZeros + neededZeros > seq.size()/2) || (numOnes + neededOnes > seq.size()/2)) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }

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
