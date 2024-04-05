package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;
import edu.rpi.legup.puzzle.thermometer.ThermometerVial;

import java.util.ArrayList;

//TODO: Rule is unimplemented
public class FinishWithMercuryDirectRule extends DirectRule{
    public FinishWithMercuryDirectRule() {
        super(
                "THERM-BASC-0003",
                "Finish with Mercury",
                "Remaining tiles must be filled to satisfy requirement",
                "edu/rpi/legup/images/thermometer/FinishWithMercury.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {return null;}

    @Override
    public Board getDefaultBoard(TreeNode node) {return null;}
}
