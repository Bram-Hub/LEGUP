package puzzles.testpuzzle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.List;

public class ValidCaseRule extends CaseRule {
    public ValidCaseRule(String ruleID, String ruleName, String description, String imageName) {
        super("TEST-CASE-0001",
            "Valid Case",
            "Returns null",
            "edu/rpi/legup/images/nurikabe/cases/FinishRoom.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        return null;
    }

    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
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
