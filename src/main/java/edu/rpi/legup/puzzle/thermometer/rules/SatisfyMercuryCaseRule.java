package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.*;
import java.util.ArrayList;
//TODO:This rule is unimplemented
public class SatisfyMercuryCaseRule extends CaseRule {
    public SatisfyMercuryCaseRule() {
        super("THERM-CASE-0002",
                "Satisfy Mercury",
                "There are multiple ways column/row requirements can be fufilled",
                "edu/rpi/legup/images/thermometer/SatisfyMercury.png");
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {return null;}

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {return null;}

    @Override
    public CaseBoard getCaseBoard(Board board) {return null;}

}
