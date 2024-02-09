package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.DirectRule;
import edu.rpi.legup.model.ContradictionRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

public class OneTileGapDirectRule extends DirectRule {

    public OneTileGapDirectRule() {
        super("BINA-BASC-0002",
                "One Tile Gap",
                "If an empty tile is surrounded by the same value, fill the gap with the other value.",
                "FILL IN WITH IMAGE");
    }
}