package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

public class EliminateTheImpossibleDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public EliminateTheImpossibleDirectRule() {
        super(
                "BINA-BASC-0004",
                "Eliminate The Impossible",
                "Out of the remaining empty cells in this row or column, this digit must go here, otherwise there will be a future contradiction",
                "edu/rpi/legup/images/binary/rules/EliminateTheImpossibleDirectRule.png");
    }

    /**
     * Function to generate all possible binary strings with a given number of zeros
     * and ones.
     *
     * @param spots         The total number of spots (length of the binary string).
     * @param possibilities An ArrayList that will be populated with the possible
     *                      results.
     * @param zeroCount     The number of zeros required.
     * @param oneCount      The number of ones required.
     */
    void generatePossibilities(int spots, ArrayList<String> possibilities, int zeroCount, int oneCount) {
        if (zeroCount + oneCount != spots) {
            System.out.println("INVALID INPUT");
            return;
        }

        // Add the string consisting of all zeros if applicable
        if (zeroCount == spots) {
            String zero = "";
            for (int i = 0; i < spots; i++) {
                zero = zero + "0";
            }
            possibilities.add(zero);
        }

        // Generate all binary strings of length 'spots'
        int count = (int) Math.pow(2, spots) - 1;
        int finalLen = spots;
        Queue<String> q = new LinkedList<>();
        q.add("1");

        while (count-- > 0) {
            String s1 = q.peek();
            q.remove();

            String newS1 = s1;
            int curLen = newS1.length();
            int runFor = spots - curLen;
            if (curLen < finalLen) {
                for (int i = 0; i < runFor; i++) {
                    newS1 = "0" + newS1;
                }
            }

            int curZeros = 0;
            int curOnes = 0;
            for (int i = 0; i < spots; i++) {
                if (newS1.charAt(i) == '0') {
                    curZeros++;
                }
                if (newS1.charAt(i) == '1') {
                    curOnes++;
                }
            }

            if (zeroCount == curZeros && oneCount == curOnes) {
                possibilities.add(newS1);
            }
            q.add(s1 + "0");
            q.add(s1 + "1");
        }
    }

    /**
     * Checks if a binary string combination is valid based on the rule that no
     * three consecutive
     * digits should be the same.
     *
     * @param cells       The current state of the row or column cells.
     * @param combination The binary string combination to check.
     * @return True if the combination is valid, false otherwise.
     */
    private boolean isValidCombination(ArrayList<BinaryCell> cells, String combination) {
        int count = 1;
        for (int i = 1; i < combination.length(); i++) {
            if (combination.charAt(i) == combination.charAt(i - 1)) {
                count++;
                if (count == 3) {
                    return false;
                }
            } else {
                count = 1;
            }
        }
        return true;
    }

    /**
     * Filters out the invalid combinations that have three consecutive identical
     * digits.
     *
     * @param combinations The list of all possible combinations.
     * @param cells        The current state of the row or column cells.
     * @return The list of valid combinations.
     */
    private ArrayList<String> filterValidCombinations(ArrayList<String> combinations, ArrayList<BinaryCell> cells) {
        ArrayList<String> validCombinations = new ArrayList<>();
        for (String combination : combinations) {
            if (isValidCombination(cells, combination)) {
                validCombinations.add(combination);
            }
        }
        return validCombinations;
    }

    /**
     * Checks the validity of a row configuration for the given binary cell.
     *
     * @param origBoard  The original board state.
     * @param binaryCell The cell to check.
     * @return True if the row configuration is valid, false otherwise.
     */
    private boolean checkValidityForRow(BinaryBoard origBoard, BinaryCell binaryCell) {
        ArrayList<BinaryCell> row = origBoard.listRowCells(binaryCell.getLocation().y);

        int size = row.size();
        int rowNumZeros = 0;
        int rowNumOnes = 0;

        // Count the number of zeros and ones in the row
        for (BinaryCell item : row) {
            if (item.getType() == BinaryType.ZERO) {
                rowNumZeros++;
            } else if (item.getType() == BinaryType.ONE) {
                rowNumOnes++;
            }
        }

        ArrayList<String> rowResult = new ArrayList<>();
        generatePossibilities((size - rowNumZeros - rowNumOnes), rowResult, size / 2 - rowNumZeros,
                size / 2 - rowNumOnes);

        ArrayList<String> validRowCombinations = filterValidCombinations(rowResult, row);

        int colNum = binaryCell.getLocation().x;
        // Check if the column value of the binary cell is valid in all valid row
        // combinations
        for (String combination : validRowCombinations) {
            if (combination.charAt(colNum) != (char) (binaryCell.getData() + '0')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the validity of a column configuration for the given binary cell.
     *
     * @param origBoard  The original board state.
     * @param binaryCell The cell to check.
     * @return True if the column configuration is valid, false otherwise.
     */
    private boolean checkValidityForColumn(BinaryBoard origBoard, BinaryCell binaryCell) {
        ArrayList<BinaryCell> col = origBoard.listColCells(binaryCell.getLocation().x);

        int size = col.size();
        int colNumZeros = 0;
        int colNumOnes = 0;

        // Count the number of zeros and ones in the column
        for (BinaryCell item : col) {
            if (item.getType() == BinaryType.ZERO) {
                colNumZeros++;
            } else if (item.getType() == BinaryType.ONE) {
                colNumOnes++;
            }
        }

        ArrayList<String> colResult = new ArrayList<>();
        generatePossibilities((size - colNumZeros - colNumOnes), colResult, size / 2 - colNumZeros,
                size / 2 - colNumOnes);

        ArrayList<String> validColCombinations = filterValidCombinations(colResult, col);

        int rowNum = binaryCell.getLocation().y;
        // Check if the row value of the binary cell is valid in all valid column
        // combinations
        for (String combination : validColCombinations) {
            if (combination.charAt(rowNum) != (char) (binaryCell.getData() + '0')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the rule for the given tree transition and puzzle element.
     *
     * @param transition    The current tree transition.
     * @param puzzleElement The puzzle element to check.
     * @return A message if the rule is invalid, null otherwise.
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        boolean validRow = checkValidityForRow(origBoard, binaryCell);
        boolean validColumn = checkValidityForColumn(origBoard, binaryCell);

        // If both row and column configurations are valid, return null (no error)
        if (validRow && validColumn) {
            return null;
        }

        // If either the row or column configuration is invalid, return the error
        // message
        return INVALID_USE_MESSAGE;
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
