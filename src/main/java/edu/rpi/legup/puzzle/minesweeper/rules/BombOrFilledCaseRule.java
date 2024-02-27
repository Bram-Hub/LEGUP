package edu.rpi.legup.puzzle.minesweeper.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.List;

public class BombOrFilledCaseRule extends CaseRule {

    public BombOrFilledCaseRule() {
        super("MINE-CASE-0000", "Bomb Or Filled",
                "A cell can either be a bomb or filled.\n",
                "");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        return null;
    }

    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
