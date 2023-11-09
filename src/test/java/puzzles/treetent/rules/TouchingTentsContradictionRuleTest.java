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
import edu.rpi.legup.puzzle.treetent.rules.TouchingTentsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class TouchingTentsContradictionRuleTest {

    private static final TouchingTentsContradictionRule RULE = new TouchingTentsContradictionRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    //DIAGONAL TESTS
    /**
     * @throws InvalidFileFormatException
     * Tests a tent diagonal of orientation T
     *                                       T
     **/
    @Test
    public void TouchingTentsContradictionRule_DiagonalUpLeftToDownRight() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsDiagonal", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests a tent diagonal of orientation  T
     *                                      T
     **/
    @Test
    public void TouchingTentsContradictionRule_DiagonalDownLeftToUpRight() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsDiagonalAlt",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(1,0)));
        Assert.assertNull(RULE.checkRuleAt(transition,board.getCell(0,1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    //ADJACENT TESTS
    /**
     * @throws InvalidFileFormatException
     * Tests a tent adjacent of orientation  T
     *                                       T
     **/
    @Test
    public void TouchingTentsContradictionRule_AdjacentVertical() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsAdjacent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }

    /**
     * @throws InvalidFileFormatException
     * Tests a tent adjacent of orientation  TT
     **/
    @Test
    public void TouchingTentsContradictionRule_AdjacentHorizontal() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsAdjacentAlt", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }
    //MIXED TESTS
    /**
     * @throws InvalidFileFormatException
     * Tests a tent of orientation  TT
     *                              TT
     **/
    @Test
    public void TouchingTentsContradictionRule_2By2Square() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsFull2By2",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests a tent of orientation  TT
     *                              T
     **/
    @Test
    public void TouchingTentsContradictionRule_UpLeft() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsMixedUpLeft",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests a tent of orientation  TT
     *                               T
     **/
    @Test
    public void TouchingTentsContradictionRule_UpRight() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsMixedUpRight",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests a tent of orientation  T
     *                              TT
     **/
    @Test
    public void TouchingTentsContradictionRule_DownLeft() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsMixedDownLeft",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests a tent of orientation   T
     *                              TT
     **/
    @Test
    public void TouchingTentsContradictionRule_DownRight() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsMixedDownRight",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests if tree adjacent triggers a null
     */
    @Test
    public void TouchingTentsContradictionRule_TreeAdjacent() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsTreeAdjacent",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNotNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 1)));
    }
    /**
     * @throws InvalidFileFormatException
     * Tests if tree diagonal triggers a null
     */
    @Test
    public void TouchingTentsContradictionRule_TreeDiagonal() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/treetent/rules/TouchingTentsContradictionRule/TouchingTentsTreeDiagonal",treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNotNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(1, 0)));
    }

}


