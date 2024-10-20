package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import java.util.ArrayList;

public class UniqueRowColumnDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public UniqueRowColumnDirectRule() {
        super(
                "BINA-BASC-0004",
                "Unique Row/Column",
                "If an unfinished row/column only differs by empty cells from a finished one, "
                        + "fill contradicting empty cells with opposite digit to prevent a repeated row/column",
                "edu/rpi/legup/images/binary/rules/UniqueRowColumnDirectRule.png");
    }

    /**
     * Counts the number of empty (UNKNOWN) cells in a given sequence
     *
     * @param seq The sequence of BinaryType elements to check
     * @return The number of empty (UNKNOWN) cells in the sequence
     */
    private int getNumEmpty(ArrayList<BinaryType> seq) {
        int numEmpty = 0;
        for (BinaryType t : seq) {
            if (t.equals(BinaryType.UNKNOWN)) {
                numEmpty++;
            }
        }
        return numEmpty;
    }

    /**
     * Checks if there is a valid opposite digit to prevent a repeated row/column
     *
     * @param seq The sequence (row or column) to check
     * @param origBoard The original board
     * @param binaryCell The binary cell being checked
     * @param rowOrColumn Flag to indicate whether checking a row (0) or a column (1)
     * @return Null if a valid opposite digit is found, otherwise an error message
     */
    private String checkOppositeDigitDifference(
            ArrayList<BinaryType> seq,
            BinaryBoard origBoard,
            BinaryCell binaryCell,
            int rowOrColumn) {
        // rowOrColumn : 0 for row, 1 for column

        int numEmpty = getNumEmpty(seq);
        if (numEmpty > 2) {
            return "Row/Column must have at most 2 empty cells";
        }

        boolean valid = false;
        for (int i = 0; i < seq.size(); i++) {
            ArrayList<BinaryType> currSeq;
            // Get the sequence (row or column) from the original board to compare
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
                // If the current sequence has empty cells, it's not valid for comparison
                if (numEmptyInCurrSeq != 0) {
                    valid = false;
                    break;
                }
                // Count differences between the sequences, stopping if more than 1 difference is
                // found
                if (!seq.get(j).equals(currSeq.get(j)) && !seq.get(j).equals(BinaryType.UNKNOWN)) {
                    if (++numDifferentCells > 1 || numEmpty != 1) {
                        valid = false;
                        break;
                    }
                }

                // Check if there's a contradiction with the current cell, if not mark as valid
                if (currSeq.get(j).equals(BinaryType.ZERO)
                        && seq.get(j).equals(BinaryType.UNKNOWN)
                        && binaryCell.getType().equals(BinaryType.ONE)) {
                    if ((rowOrColumn == 0 && binaryCell.getLocation().x == j)
                            || rowOrColumn == 1 && binaryCell.getLocation().y == j) {
                        valid = true;
                    }
                } else if (currSeq.get(j).equals(BinaryType.ONE)
                        && seq.get(j).equals(BinaryType.UNKNOWN)
                        && binaryCell.getType().equals(BinaryType.ZERO)) {
                    if ((rowOrColumn == 0 && binaryCell.getLocation().x == j)
                            || rowOrColumn == 1 && binaryCell.getLocation().y == j) {
                        valid = true;
                    }
                }
            }
            // Exit if a valid sequence is found
            if (valid) {
                break;
            }
        }

        if (valid) {
            return null;
        }
        return "There does not exist an opposite digit difference in ";
    }

    /**
     * Checks if there is one digit remaining in a sequence that can be filled to avoid repeating
     * another sequence on the board
     *
     * @param seq The sequence (row or column) to check
     * @param origBoard The original board
     * @param binaryCell The binary cell being checked
     * @param rowOrColumn Flag to indicate whether checking a row (0) or a column (1)
     * @param zeroOrOne Flag to indicate whether checking for 0s (0) or 1s (1)
     * @return Null if the rule can be applied, otherwise an error message
     */
    private String checkRemainingOneDigitDifference(
            ArrayList<BinaryType> seq,
            BinaryBoard origBoard,
            BinaryCell binaryCell,
            int rowOrColumn,
            int zeroOrOne) {
        // zeroOrOne: zero for 0, one for 1

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

            boolean valid = true;
            for (int j = 0; j < currSeq.size(); j++) {
                int numEmptyInCurrSeq = getNumEmpty(currSeq);
                // If the current sequence has empty cells, it's not valid for comparison
                if (numEmptyInCurrSeq != 0) {
                    valid = false;
                    break;
                }
                // Check if there is a cell difference from this seq and current seq
                if (!seq.get(j).equals(currSeq.get(j)) && !seq.get(j).equals(BinaryType.UNKNOWN)) {
                    valid = false;
                    break;
                }
            }
            // Determine if the current sequence can be modified to prevent repetition
            if (valid) {
                BinaryType currSeqCell = currSeq.get(binaryCell.getLocation().x);
                if (rowOrColumn == 0) {
                    currSeqCell = currSeq.get(binaryCell.getLocation().x);
                } else if (rowOrColumn == 1) {
                    currSeqCell = currSeq.get(binaryCell.getLocation().y);
                }

                // Check if this sequence has only one more zero remaining and current sequence
                // fills that zero in,
                // if so, zero in this seq must go in another cell to prevent repetition
                if (zeroOrOne == 0) {
                    if (currSeqCell.equals(BinaryType.ZERO)
                            && binaryCell.getType().equals(BinaryType.ONE)) {
                        return null;
                    }
                }
                // Check if this sequence has only one more one remaining and current sequence fills
                // that one in,
                // if so, one in this seq must go in another cell to prevent repetition
                else if (zeroOrOne == 1) {
                    if (currSeqCell.equals(BinaryType.ONE)
                            && binaryCell.getType().equals(BinaryType.ZERO)) {
                        return null;
                    }
                }
            }
        }

        return "There does not exist a sequence that can be prevented by a remaining digit difference";
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        // Check if filling the current cell with the opposite digit would prevent repetition with
        // another row
        ArrayList<BinaryType> row = origBoard.getRowTypes(binaryCell.getLocation().y);
        if (checkOppositeDigitDifference(row, origBoard, binaryCell, 0) == null) {
            return null;
        }
        int numZeros = 0;
        int numOnes = 0;
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).equals(BinaryType.ZERO)) {
                numZeros++;
            } else if (row.get(i).equals(BinaryType.ONE)) {
                numOnes++;
            }
        }

        // Check if only one more zero is needed, then see this row will be repeated by another row
        // if current cell is filled in with last zero as well
        if (numZeros == row.size() / 2 - 1) {
            if (checkRemainingOneDigitDifference(row, origBoard, binaryCell, 0, 0) == null) {
                return null;
            }
        }

        // Check if only one more one is needed, then see this row will be repeated by another row
        // if current cell is filled in with last one as well
        if (numOnes == row.size() / 2 - 1) {
            if (checkRemainingOneDigitDifference(row, origBoard, binaryCell, 0, 1) == null) {
                return null;
            }
        }

        // Check if filling the current cell with the opposite digit would prevent repetition with
        // another column
        ArrayList<BinaryType> col = origBoard.getColTypes(binaryCell.getLocation().x);
        if (checkOppositeDigitDifference(col, origBoard, binaryCell, 1) == null) {
            return null;
        }

        numZeros = 0;
        numOnes = 0;
        for (int i = 0; i < col.size(); i++) {
            if (col.get(i).equals(BinaryType.ZERO)) {
                numZeros++;
            } else if (col.get(i).equals(BinaryType.ONE)) {
                numOnes++;
            }
        }

        // Check if only one more zero is needed, then see this column will be repeated by another
        // column
        // if current cell is filled in with last zero as well
        if (numZeros == col.size() / 2 - 1) {
            if (checkRemainingOneDigitDifference(col, origBoard, binaryCell, 1, 0) == null) {
                return null;
            }
        }

        // Check if only one more one is needed, then see this column will be repeated by another
        // column
        // if current cell is filled in with last one as well
        if (numOnes == col.size() / 2 - 1) {
            if (checkRemainingOneDigitDifference(col, origBoard, binaryCell, 1, 1) == null) {
                return null;
            }
        }

        return "There is no row/column that forces this cell to be a "
                + binaryCell.getData().toString();
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link
     * TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
