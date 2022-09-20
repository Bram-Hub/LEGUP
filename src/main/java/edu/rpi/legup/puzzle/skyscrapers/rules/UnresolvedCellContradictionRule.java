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

public class UnresolvedCellContradictionRule extends ContradictionRule {

    public UnresolvedCellContradictionRule() {
        super("SKYS-CONT-0004", "Unresolved Cell",
                "Elimination leaves no possible number for a cell.",
                "edu/rpi/legup/images/skyscrapers/contradictions/UnresolvedCell.png");
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
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        Set<Integer> candidates = new HashSet<Integer>();

        //check row
        for (int i = 0; i < skyscrapersboard.getWidth(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(i, loc.y);
            if (i != loc.x && cell.getType() == SkyscrapersType.UNKNOWN && c.getType() == SkyscrapersType.Number) {
                //System.out.print(c.getData());
                //System.out.println(cell.getData());
                candidates.add(c.getData());
            }
        }

        // check column
        for (int i = 0; i < skyscrapersboard.getHeight(); i++) {
            SkyscrapersCell c = skyscrapersboard.getCell(loc.x, i);
            if (i != loc.y && cell.getType() == SkyscrapersType.UNKNOWN && c.getType() == SkyscrapersType.Number) {
                //System.out.print(c.getData());
                //System.out.println(cell.getData());
                candidates.add(c.getData());
            }
        }

        if (candidates.size() == skyscrapersboard.getWidth()) {
            System.out.print("violation");
            return null;
        }
        //System.out.print("Does not contain a contradiction at this index");
        return super.getNoContradictionMessage();
    }
}
