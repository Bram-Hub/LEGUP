package edu.rpi.legup.puzzle.skyscrapers.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class UnresolvedNumberContradictionRule extends ContradictionRule {

    public UnresolvedNumberContradictionRule() {
        super("SKYS-CONT-0005", "Unresolved Cell",
                "No possible cell for a number without a duplicate contradiction.",
                //specify a number? defaulting to every number for now. expand to more than duplicate?
                "edu/rpi/legup/images/skyscrapers/contradictions/UnresolvedNumber.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        //maybe: override check contradiction to only rows/cols for each number
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        //each possible value
        for (int i = 0; i < skyscrapersboard.getWidth(); i++) {

        }


        return super.getNoContradictionMessage();
    }
}

