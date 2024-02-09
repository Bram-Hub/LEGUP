package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class OneOrZeroCaseRule extends CaseRule {

    public OneOrZeroCaseRule() {
        super("BINA-CASE-0001",
                "One or Zero",
                "Each blank cell is either a one or a zero.",
                "FILL IN WITH IMAGE");
    }
}
