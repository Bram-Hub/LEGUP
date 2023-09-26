package puzzles.nurikabe.rules;

import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import legup.MockGameBoardFacade;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.CornerBlackDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import edu.rpi.legup.puzzle.nurikabe.NurikabeCell;

import java.awt.*;


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
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/TooFewSpacesContradictionRule/TwoSurroundBlack", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        transition.setRule(RULE);

        NurikabeCell cell = board.getCell(2, 0);
        cell.setData(NurikabeType.BLACK.toValue());
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for(int i = 0; i < board.getHeight(); i++) {
            for(int k = 0; k < board.getWidth(); k++) {
                Point point  = new Point(k, i);
                if(point.equals(cell)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
