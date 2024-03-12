package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;
import edu.rpi.legup.puzzle.binary.BinaryType;

import java.util.ArrayList;
import java.util.List;

public class OneOrZeroCaseRule extends CaseRule {

    public OneOrZeroCaseRule() {
        super("BINA-CASE-0001",
                "One or Zero",
                "Each blank cell is either a one or a zero.",
                "edu/rpi/legup/images/binary/rules/OneOrZeroCaseRule.png");
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        return null;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }    
}
