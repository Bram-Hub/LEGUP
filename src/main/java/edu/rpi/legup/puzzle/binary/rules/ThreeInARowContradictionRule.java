package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class ThreeInARowContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "";
    private final String INVALID_USE_MESSAGE = "Does not contain a contradiction at this index";

    public ThreeInARowContradictionRule() {
        super("BINA-CONT-0001",
                "Three In A Row",
                "Three of the same value cannot exist in a row",
                "FILL IN WITH IMAGE");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        return null;
    }
}