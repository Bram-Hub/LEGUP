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
public class ThermometerTooLargeDirectRule extends DirectRule{
    public ThermometerTooLargeDirectRule() {
        super(
                "THERM-BASC-0006",
                "Minimum Fill",
                "Some thermometers must be filled a minimum amount to satisfy requirement",
                "edu/rpi/legup/images/thermometer/MinimumFill.png");
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {return null;}

    @Override
    public Board getDefaultBoard(TreeNode node) {return null;}
}