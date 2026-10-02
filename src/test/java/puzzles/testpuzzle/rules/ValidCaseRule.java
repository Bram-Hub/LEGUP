package puzzles.testpuzzle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import java.util.ArrayList;

public class ValidCaseRule extends CaseRule {
    public ValidCaseRule() {
        super(
                "TEST-CASE-0001",
                "Valid Case",
                "Returns null",
                "edu/rpi/legup/images/nurikabe/cases/FinishRoom.png");
    }

    @Override
    public CaseBoard getApplicableLocationsBoard(Board board) {
        return null;
    }

    @Override
    public ArrayList<Board> getCasesFrom(Board board, PuzzleElement puzzleElement) {
        return null;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}
