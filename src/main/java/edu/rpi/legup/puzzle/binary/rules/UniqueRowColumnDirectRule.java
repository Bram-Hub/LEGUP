package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.Binary;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UniqueRowColumnDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public UniqueRowColumnDirectRule() {
        super(
                "BINA-BASC-0004",
                "Unique Row/Column",
                "If an unfinished row/column only differs by empty cells from a finished one, fill contradicting empty cells with opposite digit to prevent a repeated row/column",
                "edu/rpi/legup/images/binary/rules/UniqueRowColumnDirectRule.png");
    }

    private int getNumEmpty(ArrayList<BinaryType> seq) {
        int numEmpty = 0;
        for (BinaryType t : seq) {
            if (t.equals(BinaryType.UNKNOWN)) {
                numEmpty++;
                if (numEmpty > 2) {
                    break;
                }
            }
        }
        return numEmpty;
    }
    private String checkOppositeDigitDifference(ArrayList<BinaryType> seq, BinaryBoard origBoard, BinaryCell binaryCell, int rowOrColumn) {
        // rowOrColumn : 0 for row, 1 for column

        int numEmpty = getNumEmpty(seq);
        if (numEmpty > 2) {
            return "Row/Col must have at most 2 empty cells";
        }

        boolean valid = false;
        for (int i = 0; i < seq.size(); i++) {
            ArrayList<BinaryType> currSeq;
            if (rowOrColumn == 0) {
                if (i == binaryCell.getLocation().y) {
                    continue;
                }
                currSeq = origBoard.getRowTypes(i);
            } else {
                if (i == binaryCell.getLocation().x) {
                    continue;
                }
                currSeq = origBoard.getColTypes(i);
            }

            int numDifferentCells = 0;
            for (int j = 0; j < currSeq.size(); j++) {
                int numEmptyInCurrSeq = getNumEmpty(currSeq);
                if (numEmptyInCurrSeq != 0) {
                    valid = false;
                    break;
                }
                if (!seq.get(j).equals(currSeq.get(j)) && !seq.get(j).equals(BinaryType.UNKNOWN)) {
                    if (++numDifferentCells > 1 || numEmpty != 1) {
                        valid = false;
                        break;
                    }
                }

                if (currSeq.get(j).equals(BinaryType.ZERO) && seq.get(j).equals(BinaryType.UNKNOWN) && binaryCell.getType().equals(BinaryType.ONE)) {
                    if ((rowOrColumn == 0 && binaryCell.getLocation().x == j) || rowOrColumn == 1 && binaryCell.getLocation().y == j) {
                        valid = true;
                    }
                }
                else if (currSeq.get(j).equals(BinaryType.ONE) && seq.get(j).equals(BinaryType.UNKNOWN) && binaryCell.getType().equals(BinaryType.ZERO)) {
                    if ((rowOrColumn == 0 && binaryCell.getLocation().x == j) || rowOrColumn == 1 && binaryCell.getLocation().y == j) {
                        valid = true;
                    }
                }
            }
            if (valid) {
                break;
            }
        }

        if (valid) {
            return null;
        }
        return "Rule is not applicable";
    }

    private String checkRemainingOneDigitDifference(ArrayList<BinaryType> seq, BinaryBoard origBoard, BinaryCell binaryCell, int rowOrColumn, int zeroOrOne) {
        // zeroOrOne: zero for 0, one for 1

        for (int i = 0; i < seq.size(); i++) {
            ArrayList<BinaryType> currSeq;
            if (rowOrColumn == 0) {
                if (i == binaryCell.getLocation().y) {
                    continue;
                }
                currSeq = origBoard.getRowTypes(i);
            }
            else {
                if (i == binaryCell.getLocation().x) {
                    continue;
                }
                currSeq = origBoard.getColTypes(i);
            }

            boolean valid = true;
            for (int j = 0; j < currSeq.size(); j++) {
                int numEmptyInCurrSeq = getNumEmpty(currSeq);
                if (numEmptyInCurrSeq != 0) {
                    valid = false;
                    break;
                }
                if (!seq.get(j).equals(currSeq.get(j)) && !seq.get(j).equals(BinaryType.UNKNOWN)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                BinaryType currSeqCell = currSeq.get(binaryCell.getLocation().x);
                if (rowOrColumn == 0) {
                    currSeqCell = currSeq.get(binaryCell.getLocation().x);
                } else if (rowOrColumn == 1) {
                    currSeqCell = currSeq.get(binaryCell.getLocation().y);
                }

                if (zeroOrOne == 0) {
                    if (currSeqCell.equals(BinaryType.ZERO) && binaryCell.getType().equals(BinaryType.ONE)) {
                        return null;
                    }
                }
                else if (zeroOrOne == 1) {
                    if (currSeqCell.equals(BinaryType.ONE) && binaryCell.getType().equals(BinaryType.ZERO)) {
                        return null;
                    }
                }
            }
        }
        return "Rule is not applicable";
    }


    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        ArrayList<BinaryType> row = origBoard.getRowTypes(binaryCell.getLocation().y);
        if (checkOppositeDigitDifference(row, origBoard, binaryCell, 0) == null) {
            return null;
        }
        int numZeros = 0;
        int numOnes = 0;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).equals(BinaryType.ZERO)) {
                numZeros++;
            }
            else if (row.get(i).equals(BinaryType.ONE)) {
                numOnes++;
            }
        }

        if (numZeros == row.size()/2 - 1) {
            if (checkRemainingOneDigitDifference(row, origBoard, binaryCell, 0, 0) == null) {
                return null;
            }
        }
        if (numOnes == row.size()/2 - 1) {
            if (checkRemainingOneDigitDifference(row, origBoard, binaryCell, 0, 1) == null) {
                return null;
            }
        }

        ArrayList<BinaryType> col = origBoard.getColTypes(binaryCell.getLocation().x);
        if (checkOppositeDigitDifference(col, origBoard, binaryCell, 1) == null) {
            return null;
        }

        numZeros = 0;
        numOnes = 0;
        for (int i = 0; i < col.size(); i++) {
            if (col.get(i).equals(BinaryType.ZERO)) {
                numZeros++;
            }
            else if (col.get(i).equals(BinaryType.ONE)) {
                numOnes++;
            }
        }

        if (numZeros == col.size()/2 - 1) {
            if (checkRemainingOneDigitDifference(col, origBoard, binaryCell, 1, 0) == null) {
                return null;
            }
        }
        if (numOnes == col.size()/2 - 1) {
            if (checkRemainingOneDigitDifference(col, origBoard, binaryCell, 1, 1) == null) {
                return null;
            }
        }

        return "Rule is not applicable";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
