package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class DuplicateColumnsContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Column must have a value in each cell";

    public DuplicateColumnsContradictionRule() {
        super("BINA-CONT-0006",
                "Duplicate Columns",
                "There must not be two columns that are duplicates",
                "currentlynoimage.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        BinaryBoard binaryBoard = (BinaryBoard) board;

        BinaryCell cell = (BinaryCell) binaryBoard.getPuzzleElement(puzzleElement);
        Set<BinaryCell> col = binaryBoard.getCol(cell.getLocation().x);
        BinaryCell[] colArray = col.toArray(new BinaryCell[0]);
        int size = col.size();
        int x = cell.getLocation().x;
        for (int i = 0; i < size; i++) {
            if (i != x) {
                Set<BinaryCell> currCol = binaryBoard.getCol(i);
                BinaryCell[] currColArray = currCol.toArray(new BinaryCell[0]);
                for (int j = 0; j < size; j++) {
                    BinaryCell colElement = colArray[j];
                    BinaryCell currColElement = currColArray[j];
                    if (colElement.getType() == BinaryType.UNKNOWN || currColElement.getType() == BinaryType.UNKNOWN) {
                        return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
                    }
                    if (colElement.getType() != currColElement.getType()) {
                        return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
                    }
                }
            }
        }
        return null;
    }
}
