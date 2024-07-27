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
                    continue;
                }
                if (!seq.get(j).equals(currSeq.get(j)) && !seq.get(j).equals(BinaryType.UNKNOWN)) {
                    if (++numDifferentCells > 1 || numEmpty != 1) {
                        valid = false;
                        break;
                    }
                }
                System.out.println(" POS X: " + j + " Y: " + i);
                System.out.println(" CEL X: " + binaryCell.getLocation().x + " Y: " + binaryCell.getLocation().y);

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
                System.out.println(j);
            }
            if (valid) {
                break;
            }
        }

        if (!valid) {
            return "Rule is not applicable";
        }
        return null;
    }
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;


        ArrayList<BinaryType> row = origBoard.getRowTypes(binaryCell.getLocation().y);
        if (checkSequence(row, origBoard, binaryCell, 0) == null) {
            return null;
        }

        ArrayList<BinaryType> col = origBoard.getColTypes(binaryCell.getLocation().x);
        if (checkSequence(col, origBoard, binaryCell, 1) == null) {
            return null;
        }

        return "Rule is not applicable";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
