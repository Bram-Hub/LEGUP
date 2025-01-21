package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.SurroundTentWithGrassDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SurroundTentWithGrassDirectRuleTest {

    private static final SurroundTentWithGrassDirectRule RULE =
            new SurroundTentWithGrassDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * 3x3 TreeTent puzzle Tests SurroundTentWithGrassDirectRule
     *
     * <p>TREE at (0, 0), (2, 0), (0, 1), (2, 1), (1, 2), and (2, 2); TENT at (1, 1) RGR RTR GRR
     *
     * <p>Makes (1, 0) and (0, 2) GRASS Checks that the rule detects unknown adjacent and diagonal
     * tiles correctly
     */
    @Test
    public void SurroundTentWithGrassBasicRuleTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/SurroundTentWithGrassDirectRule/SurroundTentWithGrass",
                treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(0, 2);
        cell2.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle Tests SurroundTentWithGrassDirectRule
     *
     * <p>TENT at (1, 1) GGG GTG GGG
     *
     * <p>Makes all cells adjacent and diagonal to the tent GRASS Checks that the rule detects all
     * adjacent and diagonal tiles correctly
     */
    @Test
    public void SurroundTentWithGrassBasicRuleTest_BadBoard() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/SurroundTentWithGrassDirectRule/SurroundTentWithGrassBad",
                treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(0, 0);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(1, 0);
        cell2.setData(TreeTentType.GRASS);
        TreeTentCell cell3 = board.getCell(2, 0);
        cell3.setData(TreeTentType.GRASS);
        TreeTentCell cell4 = board.getCell(0, 1);
        cell4.setData(TreeTentType.GRASS);
        // Skip (1,1) due to being the Tent
        TreeTentCell cell5 = board.getCell(2, 1);
        cell5.setData(TreeTentType.GRASS);
        TreeTentCell cell6 = board.getCell(0, 2);
        cell6.setData(TreeTentType.GRASS);
        TreeTentCell cell7 = board.getCell(1, 2);
        cell7.setData(TreeTentType.GRASS);
        TreeTentCell cell8 = board.getCell(2, 2);
        cell8.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        // board.addModifiedData(cell4);
        board.addModifiedData(cell5);
        board.addModifiedData(cell6);
        board.addModifiedData(cell7);
        board.addModifiedData(cell8);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())
                        || // point.equals(cell4.getLocation()) ||
                        point.equals(cell5.getLocation())
                        || point.equals(cell6.getLocation())
                        || point.equals(cell7.getLocation())
                        || point.equals(cell8.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle Tests SurroundTentWithGrassDirectRule
     *
     * <p>TENT at (1, 1); TREE on all adjacent and diagonal tiles RRR RTR RRR
     *
     * <p>Null Checks that the rule correctly detects no missing tiles
     */
    @Test
    public void SurroundTentWithGrassBasicRuleTest_FullBoard() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/SurroundTentWithGrassDirectRule/SurroundTentWithGrassTrees",
                treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }
}
