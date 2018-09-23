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

import java.awt.*;

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

        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Point point  = new Point(k, i);
                if(point.equals(cell1.getLocation()) || point.equals(cell2.getLocation()) ||
                        point.equals(cell3.getLocation()) || point.equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
