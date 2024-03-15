package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class DuplicateRowsOrColumnsContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Row or column must have a value in each cell";

    public DuplicateRowsOrColumnsContradictionRule() {
        super("BINA-CONT-0003",
                "Duplicate Rows Or Columns",
                "There must not be two rows or two columns that are duplicates",
                "edu/rpi/legup/images/binary/rules/DuplicateRowsContradictionRule.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;
        boolean rowValid = false;
        boolean colValid = false;
        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> row = binaryBoard.getRow(cell.getLocation().y);
        BinaryCell[] rowArray = row.toArray(new BinaryCell[0]);
        int size = row.size();
        int y = cell.getLocation().y;
        for (int i = 0; i < size; i++) {
            if (rowValid) {
                break;
            }
            if (i != y) {
                Set<BinaryCell> currRow = binaryBoard.getRow(i);
                BinaryCell[] currRowArray = currRow.toArray(new BinaryCell[0]);
                for (int j = 0; j < size; j++) {
                    BinaryCell rowElement = rowArray[j];
                    BinaryCell currRowElement = currRowArray[j];
                    if (rowElement.getType() != currRowElement.getType()) {
                        rowValid = true;
                    }
                }
            }
        }
        if (!rowValid) {
            return null;
        }
        Set<BinaryCell> col = binaryBoard.getCol(cell.getLocation().x);
        BinaryCell[] colArray = col.toArray(new BinaryCell[0]);
        size = col.size();
        int x = cell.getLocation().x;
        for (int i = 0; i < size; i++) {
            if (colValid) {
                break;
            }
            if (i != x) {
                Set<BinaryCell> currCol = binaryBoard.getCol(i);
                BinaryCell[] currColArray = currCol.toArray(new BinaryCell[0]);
                for (int j = 0; j < size; j++) {
                    BinaryCell colElement = colArray[j];
                    BinaryCell currColElement = currColArray[j];
                    if (colElement.getType() != currColElement.getType()) {
                        colValid = true;
                    }
                }
            }
        }
        if (!colValid) {
            return null;
        }

        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;

    }
}
