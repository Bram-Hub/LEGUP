package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import puzzle.nurikabe.Nurikabe;
import puzzle.nurikabe.rules.MultipleNumbersContradictionRule;
import save.InvalidFileFormatException;

public class MultipleNumbersContradictionRuleTest
{

    private static final MultipleNumbersContradictionRule RULE = new MultipleNumbersContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void MultipleNumbersContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/MultipleNumbersContradictionRule/MultipleNumbers", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == 0 || i == 2)
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
