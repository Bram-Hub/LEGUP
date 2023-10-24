package puzzles.treetent.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.rules.TooFewTentsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class TooFewTentsContradictionRuleTest {

    private static final TooFewTentsContradictionRule RULE = new TooFewTentsContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just grass, checks if the fact it expects a tent on the y-axis is caught.
     */
    @Test
    public void TooFewTentsContradictionRule_JustY() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsJustY", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just a tent, checks if the fact it expects 2 tents on the y-axis is caught.
     * (This is an impossible situation given the constraints, but for the purposes of the test it is fine)
     */
    @Test
    public void TooFewTentsContradictionRule_WithTent() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsWithTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Using a 1x1 Puzzle Grid, which is just grass, checks if the fact it expects a tent on both x and y is caught.
     */
    @Test
    public void TooFewTentsContradictionRule_DoubleBad() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TooFewTentsContradictionRule/TooFewTentsDoubleBad", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }
}

