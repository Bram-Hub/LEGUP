package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;
import edu.rpi.legup.puzzle.nurikabe.rules.BlackSquareContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class BlackSquareContradictionRuleTest
{
    private static final BlackSquareContradictionRule RULE = new BlackSquareContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp()
    {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void BlackSquareContradictionRule_TwoSurroundBlackTest() throws InvalidFileFormatException
    {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/BlackSquareContradictionRule/SimpleBlackSquare", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        NurikabeCell cell1 = board.getCell(1, 1);
        NurikabeCell cell2 = board.getCell(1, 2);
        NurikabeCell cell3 = board.getCell(2, 1);
        NurikabeCell cell4 = board.getCell(2, 2);

        Assert.assertNull(RULE.checkContradiction(transition));

        for(int i = 0; i < transition.getBoard().getElementCount(); i++)
        {
            if(i == cell1.getIndex() || i == cell2.getIndex() ||
                    i == cell3.getIndex() || i == cell4.getIndex())
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
