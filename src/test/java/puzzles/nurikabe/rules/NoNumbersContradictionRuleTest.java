package puzzles.nurikabe.rules;

import edu.rpi.legup.puzzle.nurikabe.NurikabeBoard;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.nurikabe.Nurikabe;
import edu.rpi.legup.puzzle.nurikabe.rules.NoNumberContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class NoNumbersContradictionRuleTest {

    private static final NoNumberContradictionRule RULE = new NoNumberContradictionRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the No Number contradiction rule for a white region enclosed by black squares
     */
    @Test
    public void NoNumberContradictionRule_NoNumberSurroundBlack() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/NoNumberContradictionRule/SimpleNoNumber", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkContradiction((NurikabeBoard) transition.getBoard()));

        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        Point location = new Point(1, 1);
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(location)) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the No Number contradiction rule for a false contradiction
     */
    @Test
    public void NoNumberContradictionRule_FalseNoNumber() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/NoNumberContradictionRule/FalseNoNumber", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkRule(transition));
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    @Test
    public void NoNumberContradictionRule_FalseNoNumber2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/NoNumberContradictionRule/FalseNoNumber2", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkRule(transition));
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

}
