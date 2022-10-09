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
import edu.rpi.legup.puzzle.nurikabe.rules.CornerBlackBasicRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

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
//        TestUtilities.importTestBoard("puzzles/nurikabe/rules/TooFewSpacesContradictionRule/TwoSurroundBlack", nurikabe);
//        TreeNode rootNode = nurikabe.getTree().getRootNode();
//        TreeTransition transition = rootNode.getChildren().get(0);
//        transition.setRule(RULE);
//
//        transition.getBoard().getModifiedData().clear();
//
//        Assert.assertNull(RULE.checkRule(transition));
//
//        NurikabeBoard board = (NurikabeBoard)transition.getBoard();
//        Point location = new Point(1, 1);
//        for(int i = 0; i < board.getHeight(); i++) {
//            for(int k = 0; k < board.getWidth(); k++) {
//                Point point  = new Point(k, i);
//                if(point.equals(location)) {
//                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                } else {
//                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
//                }
//            }
//        }
    }
}
