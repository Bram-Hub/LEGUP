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

public class DuplicateNumberContradictionRule extends ContradictionRule {

    public DuplicateNumberContradictionRule() {
        super("SKYS-CONT-0001", "Duplicate Number",
                "Skyscrapers of same height cannot be placed in the same row or column.",
                "edu/rpi/legup/images/skyscrapers/contradictions/DuplicateNumber.png");
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
        //TODO:? Refactor to count each row/col once rather than per cell (override checkContradiction)
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        Set<Integer> candidates = new HashSet<Integer>();

        //check row
        for (int i = 0; i < skyscrapersboard.getWidth(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(i, loc.y);
            if (i != loc.x && cell.getType() == SkyscrapersType.Number && c.getType() == SkyscrapersType.Number && c.getData() == cell.getData()) {
                System.out.print(c.getData());
                System.out.println(cell.getData());
                return null;
            }
        }

        // check column
        for (int i = 0; i < skyscrapersboard.getHeight(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(loc.x, i);
            if (i != loc.y && cell.getType() == SkyscrapersType.Number && c.getType() == SkyscrapersType.Number && c.getData() == cell.getData()) {
                System.out.print(c.getData());
                System.out.println(cell.getData());
                return null;
            }
        }

        return super.getNoContradictionMessage();
    }
}
