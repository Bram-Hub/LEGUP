package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;
import java.util.ArrayList;

public class DuplicateRowsOrColumnsContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE =
            "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row or column must have a value in each cell";

    public DuplicateRowsOrColumnsContradictionRule() {
        super(
                "BINA-CONT-0003",
                "Duplicate Rows Or Columns",
                "There must not be two rows or two columns that are duplicates",
                "edu/rpi/legup/images/binary/rules/DuplicateRowOrColumnContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);

        ArrayList<BinaryType> row = binaryBoard.getRowTypes(cell.getLocation().y);

        int size = row.size();

        for (int i = 0; i < size; i++) {
            if (i > cell.getLocation().y) {
                ArrayList<BinaryType> currRow = binaryBoard.getRowTypes(i);
                if (currRow.equals(row)) {
                    return null;
                }
            }
        }

        ArrayList<BinaryType> col = binaryBoard.getColTypes(cell.getLocation().x);

        for (int i = 0; i < size; i++) {
            if (i > cell.getLocation().x) {
                ArrayList<BinaryType> currCol = binaryBoard.getColTypes(i);
                if (currCol.equals(col)) {
                    return null;
                }
            }
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
    }
}
