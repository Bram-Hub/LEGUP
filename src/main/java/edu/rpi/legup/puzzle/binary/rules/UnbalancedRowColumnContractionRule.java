package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.nurikabe.BinaryBoard;
import edu.rpi.legup.puzzle.nurikabe.BinaryCell;
import edu.rpi.legup.puzzle.nurikabe.BinaryType;

public class UnbalancedRowColumnContradictionRule extends ContradictionRule {

    private final String NO_CONTRADICTION_MESSAGE = "";
    private final String INVALID_USE_MESSAGE = "Does not contain a contradiction at this index";

    public UnbalancedRowColumnContradictionRule() {
        super("BINA-CONT-0002",
                "Unbalanced Row Column",
                "A row or column cannot contain more of more value than the other",
                "FILL IN WITH IMAGE");
    }