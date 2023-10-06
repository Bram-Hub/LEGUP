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
import edu.rpi.legup.puzzle.treetent.rules.NoTreeForTentContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class NoTreeForTentContradictionRuleTest {

    private static final NoTreeForTentContradictionRule RULE = new NoTreeForTentContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     * Tests if, in a 2x2 Grid, a Tent in the NW corner has no adjacent trees
     */
    @Test
    public void NoTreeForTentContradictionRule_NW() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTreeForTentContradictionRule/NoTreeForTentNW", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests if, in a 2x2 Grid, a Tent in the NE corner has no adjacent trees
     */
    @Test
    public void NoTreeForTentContradictionRule_NE() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTreeForTentContradictionRule/NoTreeForTentNE", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests if, in a 2x2 Grid, a Tent in the NW corner has no adjacent trees
     */
    @Test
    public void NoTreeForTentContradictionRule_SW() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTreeForTentContradictionRule/NoTreeForTentSW", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests if, in a 2x2 Grid, a Tent in the SE corner has no adjacent trees
     */
    @Test
    public void NoTreeForTentContradictionRule_SE() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTreeForTentContradictionRule/NoTreeForTentSE", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }
    //Center of 3x3 Board
    //Test with a board with Trees all diagonal
    //Test with a board with Trees all adjacent (should assertNotNull)
    //Test with a board with Tents all adjacent
}

