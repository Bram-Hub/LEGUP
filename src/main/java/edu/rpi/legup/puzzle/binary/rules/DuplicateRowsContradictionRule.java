package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class DuplicateRowsContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row must have a value in each cell";

    public DuplicateRowsContradictionRule() {
        super("BINA-CONT-0005",
                "Duplicate Rows",
                "There must not be two rows that are duplicates",
                "edu/rpi/legup/images/binary/rules/DuplicateRowsContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> row = binaryBoard.getRow(cell.getLocation().y);
        BinaryCell[] rowArray = row.toArray(new BinaryCell[0]);
        int size = row.size();
        int y = cell.getLocation().y;
        for (int i = 0; i < size; i++) {
            if (i != y) {
                Set<BinaryCell> currRow = binaryBoard.getRow(i);
                BinaryCell[] currRowArray = currRow.toArray(new BinaryCell[0]);
                for (int j = 0; j < size; j++) {
                    BinaryCell rowElement = rowArray[j];
                    BinaryCell currRowElement = currRowArray[j];
                    if (rowElement.getType() == BinaryType.UNKNOWN || currRowElement.getType() == BinaryType.UNKNOWN) {
                        return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
                    }
                    if (rowElement.getType() != currRowElement.getType()) {
                        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
                    }
                }
            }
        }
        return null;
    }
}
