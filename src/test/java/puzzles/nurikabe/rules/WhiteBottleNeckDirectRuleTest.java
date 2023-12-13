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
import edu.rpi.legup.puzzle.nurikabe.NurikabeType;
import edu.rpi.legup.puzzle.nurikabe.rules.WhiteBottleNeckDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.awt.*;

public class WhiteBottleNeckDirectRuleTest {

    private static final WhiteBottleNeckDirectRule RULE = new WhiteBottleNeckDirectRule();
    private static Nurikabe nurikabe;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        nurikabe = new Nurikabe();
    }

    /**
     * Tests the White BottleNeck direct rule for a bottleneck in the middle of the board
     */
    @Test
    public void WhiteBottleNeckDirectRule_SimpleWhiteBottleNeckTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/WhiteBottleNeckDirectRule/SimpleWhiteBottleNeck", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        transition.setRule(RULE);

        NurikabeCell cell = board.getCell(2, 1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * Tests the White BottleNeck direct rule for a more complex board
     */
    @Test
    public void WhiteBottleNeckDirectRule_NurikabeBoard1Test() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/WhiteBottleNeckDirectRule/NurikabeBoard1", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        transition.setRule(RULE);

        NurikabeCell cell = board.getCell(0, 0);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    /**
     * Tests the White BottleNeck direct rule for a more false bottle neck
     */
    @Test
    public void WhiteBottleNeckDirectRule_FalseBottleNeck() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/nurikabe/rules/WhiteBottleNeckDirectRule/FalseBottleNeck", nurikabe);
        TreeNode rootNode = nurikabe.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        NurikabeBoard board = (NurikabeBoard) transition.getBoard();
        transition.setRule(RULE);

        NurikabeCell cell = board.getCell(2, 1);
        cell.setData(NurikabeType.WHITE.toValue());
        board.addModifiedData(cell);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }


}
