package puzzles.testpuzzle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class ValidContradictionRule extends ContradictionRule {
    public ValidContradictionRule() {
        super("TEST-CONT-0001",
            "Contradiction",
            "Always null",
            "edu/rpi/legup/images/nurikabe/contradictions/BlackSquare.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        return null;
    }
}
