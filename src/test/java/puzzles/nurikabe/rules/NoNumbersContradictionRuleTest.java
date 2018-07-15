package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.rules.NoNumberContradictionRule;
import save.InvalidFileFormatException;

public class NoNumbersContradictionRuleTest
{

    private static final NoNumberContradictionRule RULE = new NoNumberContradictionRule();
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
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/NoNumberContradictionRule/NoNumberSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
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
