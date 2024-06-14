package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;

// TODO: Rule is untested
public class TooManyMercuryContradiction extends ContradictionRule {

    private final String Invalid_Use_Message = "Mercury does not exceed limit";

    public TooManyMercuryContradiction() {
        super(
                "THERM-CONT-0003",
                "Too Many Mercury",
                "More mercury in column/row than target",
                "edu/rpi/legup/images/thermometer/TooManyMercury.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using
     * this rule
     *
     * @param board board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     *     otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        ThermometerBoard grid = (ThermometerBoard) board;
        ThermometerCell cell = (ThermometerCell) grid.getPuzzleElement(puzzleElement);
        int filled = 0;
        for (int i = 0; i < grid.getHeight(); i++) {
            if (grid.getCell((int) cell.getLocation().getX(), i).getFill()
                    == ThermometerFill.FILLED) {
                filled++;
            }
        }
        if (grid.getRowNumber((int) cell.getLocation().getX()) > filled) return null;

        filled = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            if (grid.getCell(i, (int) cell.getLocation().getY()).getFill()
                    == ThermometerFill.FILLED) {
                filled++;
            }
        }
        if (grid.getColNumber((int) cell.getLocation().getY()) > filled) return null;

        return Invalid_Use_Message;
    }
}
