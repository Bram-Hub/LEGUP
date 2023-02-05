package puzzles.nurikabe.rules;

import legup.MockGameBoardFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.CornerBlackDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

public class CornerBlackDirectRuleTest {

    private static final CornerBlackDirectRule RULE = new CornerBlackDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    @Test
    public void CornerBlackContradictionRule_SimpleCornerBlackTest() throws InvalidFileFormatException {
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
