package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.rules.CornerBlackBasicRule;
import save.InvalidFileFormatException;

public class CornerBlackBasicRuleTest
{

    private static final CornerBlackBasicRule RULE = new CornerBlackBasicRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void CornerBlackContradictionRule_SimpleCornerBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/TooFewSpacesContradictionRule/TwoSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkRule(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == 4)
            {
                Assert.assertNull(RULE.checkRuleAt(transition, i));
            }
            else
            {
                Assert.assertNotNull(RULE.checkRuleAt(transition, i));
            }
        }

    }
}
