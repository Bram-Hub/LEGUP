package puzzles.testpuzzle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

public class InvalidDirectRule extends DirectRule {
    public InvalidDirectRule() {
        super("TEST-BASC-0002", "Invalid Direct", "Always false", "edu/rpi/legup/images/nurikabe/rules/BetweenRegions.png");
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }

    @Override
    protected String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return "False";
    }
}
