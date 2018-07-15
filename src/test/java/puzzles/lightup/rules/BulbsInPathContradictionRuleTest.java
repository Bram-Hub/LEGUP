package puzzles.lightup.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.PuzzleImporter;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import puzzle.lightup.LightUp;
import puzzle.lightup.rules.BulbsInPathContradictionRule;
import save.InvalidFileFormatException;

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

        Assert.assertNull(RULE.checkContradiction(transition));
        Assert.assertNull(RULE.checkContradictionAt(transition, 0));
        Assert.assertNull(RULE.checkContradictionAt(transition, 2));

        Assert.assertNotNull(RULE.checkContradictionAt(transition, 1));
    }

    @Test
    public void BulbsInPathContradictionRule_LightInVerticalPath() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/lightup/rules/BulbsInPathContradictionRule/LightInVerticalPath", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction(transition));
        Assert.assertNull(RULE.checkContradictionAt(transition, 0));
        Assert.assertNull(RULE.checkContradictionAt(transition, 6));

        Assert.assertNotNull(RULE.checkContradictionAt(transition, 4));
    }
}
