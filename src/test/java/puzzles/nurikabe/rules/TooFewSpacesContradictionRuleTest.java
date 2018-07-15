package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.PuzzleImporter;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.rules.TooFewSpacesContradictionRule;
import save.InvalidFileFormatException;

public class TooFewSpacesContradictionRuleTest
{

    private static final TooFewSpacesContradictionRule RULE = new TooFewSpacesContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void TooFewSpacesContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/TooFewSpacesContradictionRule/TwoSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction(transition));

        for(int i = 0; i < 9; i++)
        {
            if(i == 4)
            {
                Assert.assertNull(RULE.checkContradictionAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkContradictionAt(transition, i));
            }
        }
    }
}
