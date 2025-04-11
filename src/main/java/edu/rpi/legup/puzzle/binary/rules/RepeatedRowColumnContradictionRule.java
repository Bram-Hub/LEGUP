package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import java.util.ArrayList;

public class RepeatedRowColumnContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row or column must have a value in each cell";

    public RepeatedRowColumnContradictionRule() {
        super(
                "BINA-CONT-0003",
                "Repeated Row/Column",
                "There must not be two of the same row or two of the same column in the puzzle",
                "edu/rpi/legup/images/binary/rules/RepeatedRowColumnContradictionRule.png");
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

        // Compare each row with row of current cell to see if they are equal, if so the rule is
        // applied correctly
        ArrayList<BinaryType> row = binaryBoard.getRowTypes(cell.getLocation().y);
        int size = row.size();
        for (int i = 0; i < size; i++) {
            if (i != cell.getLocation().y) {
                ArrayList<BinaryType> currRow = binaryBoard.getRowTypes(i);
                if (currRow.equals(row)) {
                    return null;
                }
            }
        }

        // Compare each column with column of current cell to see if they are equal, if so the rule
        // is applied correctly
        ArrayList<BinaryType> col = binaryBoard.getColTypes(cell.getLocation().x);
        for (int i = 0; i < size; i++) {
            if (i != cell.getLocation().x) {
                ArrayList<BinaryType> currCol = binaryBoard.getColTypes(i);
                if (currCol.equals(col)) {
                    return null;
                }
            }
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
