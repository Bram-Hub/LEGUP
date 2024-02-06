package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.Set;
public class ThreeAdjacentZerosContradictionRule extends ContradictionRule {
    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";

    public ThreeAdjacentZerosContradictionRule() {
        super("","Three Adjacent Zeros", "There cannot be three adjacent zeros in a row or column", "");
    }
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        return null;
    }
}
