package puzzles.nurikabe.rules;

import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.MultipleNumbersContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

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

        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Point point  = new Point(k, i);
                if(point.equals(new Point(0, 0)) || point.equals(new Point(2, 0))) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
