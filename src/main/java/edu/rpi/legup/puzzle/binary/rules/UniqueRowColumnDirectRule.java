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
                "BINA-BASC-0005",
                "Unique Row/Column",
                "If an unfinished row/column only differs by two empty cells from a finished one, fill empty cells with the opposite digits",
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

    private String checkSequence(ArrayList<BinaryType> seq, BinaryBoard origBoard, BinaryCell binaryCell, int rowOrColumn) {
        // rowOrColumn : 0 = row, 1 = column
        int numEmptyInRow = getNumEmpty(seq);
        if (numEmptyInRow == 2) {
            for (int i = 0; i < seq.size(); i++) {
                if (rowOrColumn == 0) {
                    if (i == binaryCell.getLocation().y) {
                        continue;
                    }
                } else {
                    if (i == binaryCell.getLocation().x) {
                        continue;
                    }
                }

                ArrayList<BinaryType> currSeq;
                if (rowOrColumn == 0) {
                    currSeq = origBoard.getRowTypes(i);
                } else {
                    currSeq = origBoard.getColTypes(i);
                }

                if (getNumEmpty(currSeq) != 0) {
                    continue;
                }

                for (int j = 0; j < seq.size(); j++) {
                    if (seq.get(j).equals(currSeq.get(j))) {
                        continue;
                    }
                    if (seq.get(j).equals(BinaryType.UNKNOWN)) {
                        if ((currSeq.get(j).equals(BinaryType.ZERO) && binaryCell.getType().equals(BinaryType.ONE) && binaryCell.getLocation().x == j) ||
                                (currSeq.get(j).equals(BinaryType.ONE) && binaryCell.getType().equals(BinaryType.ZERO) && binaryCell.getLocation().x == j)) {
                            continue;
                        }
                        else {
                            return "Duplicate row/column found";
                        }
                    }
                }
            }
        }

        return null;
    }
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;


        ArrayList<BinaryType> row = origBoard.getRowTypes(binaryCell.getLocation().y);
        int numEmptyInRow = getNumEmpty(row);
        if (numEmptyInRow != 2) {
            return "Row must have 2 empty cells";
        }

        boolean valid = false;
        for (int i = 0; i < row.size(); i++) {
            if (i == binaryCell.getLocation().y) {
                continue;
            }
            ArrayList<BinaryType> currRow;
            currRow = origBoard.getRowTypes(i);
            for (int j = 0; j < currRow.size(); j++) {
                int numEmptyInCurrRow = getNumEmpty(currRow);
                if (numEmptyInCurrRow != 0) {
                    continue;
                }
                if (!row.get(j).equals(currRow.get(j)) && !row.get(j).equals(BinaryType.UNKNOWN)) {
                    valid = false;
                    break;
                }
                System.out.println(" POS X: " + j + " Y: " + i);
                System.out.println(" CEL X: " + binaryCell.getLocation().x + " Y: " + binaryCell.getLocation().y);

                if (currRow.get(j).equals(BinaryType.ZERO) && row.get(j).equals(BinaryType.UNKNOWN) && binaryCell.getType().equals(BinaryType.ONE) && binaryCell.getLocation().x == j) {
                    System.out.println("ROW: " + i + " " + row.get(j).toString() + " = " + currRow.get(j).toString() + "?");
                    valid = true;
                }
                else if (currRow.get(j).equals(BinaryType.ONE) && row.get(j).equals(BinaryType.UNKNOWN) && binaryCell.getType().equals(BinaryType.ZERO) && binaryCell.getLocation().x == j) {
                    System.out.println("ROW: " + i + " " + row.get(j).toString() + " = " + currRow.get(j).toString() + "?");
                    valid = true;
                }
                System.out.println(j);
            }
            if (valid) {
                break;
            }
        }

        if (!valid) {
            return "Duplicate Row Found";
        }
        return null;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
