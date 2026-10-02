package puzzles.testpuzzle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;

public class InvalidContradictionRule extends ContradictionRule {
    public InvalidContradictionRule() {
        super(
                "TEST-CONT-0002",
                "No Contradiction",
                "Always empty string",
                "edu/rpi/legup/images/nurikabe/contradictions/BlackSquare.png");
    }

    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        return "No contradiction";
    }
}
