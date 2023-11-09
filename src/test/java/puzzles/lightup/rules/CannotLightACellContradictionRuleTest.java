package puzzles.lightup.rules;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.CannotLightACellContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import org.junit.BeforeClass;
import org.junit.Test;

public class CannotLightACellContradictionRuleTest {
    private static final CannotLightACellContradictionRule RULE = new CannotLightACellContradictionRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    //extensive full testing of null and non-null in a 5x5 board
    public void FullLightTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/CannotLightACellContradictionRule/FullLightTest", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        //confirm there is a contradiction somewhere on the board
        Assert.assertNull(RULE.checkContradiction(board));

        //confirm it is impossible to light up these squares
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 3)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(3, 3)));

        //confirm these are not required to be lit because they are already lit or unable to be
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(3, 2)));
    }

    @Test
    //simple contradiction testing for null and non-null in a 3x3 board
    public void CannotLightMiddleTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/CannotLightACellContradictionRule/CannotLight", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        //confirm there is a contradiction somewhere on the board
        Assert.assertNull(RULE.checkContradiction(board));

        //confirm it is impossible to light up the center square
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));

        //every square except the center
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
    }

    @Test
    public void CanLightTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/CannotLightACellContradictionRule/CanLightTest", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        //confirm there is not a contradiction somewhere on the board
        Assert.assertNotNull(RULE.checkContradiction(board));

        //confirm that these cells can be lit, are already lit, or that they are just black blocks
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 3)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(3, 3)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(3, 2)));
    }
}
