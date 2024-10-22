package puzzles.lightup.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.rules.TooManyBulbsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooManyBulbsContradictionRuleTest {
    private static final TooManyBulbsContradictionRule RULE = new TooManyBulbsContradictionRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    // complex extensive toofew test
    public void FullTooManyTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/lightup/rules/TooManyBulbsContradictionRule/FullTooManyTest", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        // confirm there is a contradiction somewhere on the board
        Assert.assertNull(RULE.checkContradiction(board));

        // confirm that there are too many bulbs around the black tiles
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 4)));

        // confirm there are no requirements for number of bulbs around non-black tiles or 0 tiles
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(5, 5)));
    }

    @Test
    // tests a 3x3 board with a 3 in the center and 4 surrounding lightbulbs
    public void TooManySimpleTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/lightup/rules/TooManyBulbsContradictionRule/TooMany", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();

        // confirm it is impossible to satisfy up the center square
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));

        // every square except the center
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
    }
}
