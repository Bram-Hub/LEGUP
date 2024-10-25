package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerFill;

// TODO: Rule is untested
public class TooFewMercuryContradiction extends ContradictionRule {

    private final String Invalid_Use_Message = "Mercury can still reach limit";

    public TooFewMercuryContradiction() {
        super(
                "THERM-CONT-0002",
                "Too Few Mercury",
                "Not enough mercury in column/row to fufill requirement",
                "edu/rpi/legup/images/thermometer/NotEnoughMercury.png");
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
    // Checks if row or column of input element has too many blocked tiles
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        ThermometerBoard grid = (ThermometerBoard) board;
        ThermometerCell cell = (ThermometerCell) grid.getPuzzleElement(puzzleElement);
        int blocked = 0;
        for (int i = 0; i < grid.getHeight(); i++) {
            if (grid.getCell((int) cell.getLocation().getX(), i).getFill()
                    == ThermometerFill.BLOCKED) {
                blocked++;
            }
        }
        if (grid.getRowNumber((int) cell.getLocation().getX()) > blocked) return null;

        blocked = 0;
        for (int i = 0; i < grid.getWidth(); i++) {
            if (grid.getCell(i, (int) cell.getLocation().getY()).getFill()
                    == ThermometerFill.BLOCKED) {
                blocked++;
            }
        }
        if (grid.getColNumber((int) cell.getLocation().getY()) > blocked) return null;

        return Invalid_Use_Message;
    }
}
