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
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.NoTentForTreeContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class NoTentForTreeContradictionRuleTest {

    private static final NoTentForTreeContradictionRule RULE = new NoTentForTreeContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     * Tests if a tree is next to only grass in a 2x2 grid triggers the contradiction
     */
    @Test
    public void NoTentForTreeContradictionRule_Basic() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTentForTreeContradictionRule/NoTentForTree", treetent);
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
     * Tests similarly to above, but now with a tent diagonally next to the tree, which should still contradict
     */
    @Test
    public void NoTentForTreeContradictionRule_Diagonal() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTentForTreeContradictionRule/NoTentForTreeDiagonal", treetent);
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
     * Tests that adjacent trees do not allow a pass
     */
    @Test
    public void NoTentForTreeContradictionRule_TwoTrees() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTentForTreeContradictionRule/NoTentForTreeTwoTrees", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests similarly to above, but now with a tent diagonally next to two trees, which should still contradict on one.
     */
    @Test
    public void NoTentForTreeContradictionRule_TwoTreesDiagonal() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/NoTentForTreeContradictionRule/NoTentForTreeTwoTreesDiagonal", treetent);
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
}

