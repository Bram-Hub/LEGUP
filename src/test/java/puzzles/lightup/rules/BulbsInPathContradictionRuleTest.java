package puzzles.lightup.rules;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.BulbsInPathContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import org.junit.BeforeClass;
import org.junit.Test;

public class BulbsInPathContradictionRuleTest
{
    private static final BulbsInPathContradictionRule RULE = new BulbsInPathContradictionRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    public void BulbsInPathContradictionRule_LightInHorizontalPath() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/lightup/rules/BulbsInPathContradictionRule/LightInHorizontalPath", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0,0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(2,0)));

        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0,1)));
    }

    @Test
    public void BulbsInPathContradictionRule_LightInVerticalPath() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/lightup/rules/BulbsInPathContradictionRule/LightInVerticalPath", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0,0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0,2)));

        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1,1)));
    }
}
