package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.FinishWithGrassDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class FinishWithGrassDirectRuleTest {

    private static final FinishWithGrassDirectRule RULE = new FinishWithGrassDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * 3x3 TreeTent puzzle with a tent at (0,0)
     * Tests FinishWithGrassDirectRule on GRASS tiles horizontal of the tent
     * at (1,0) and (2,0)
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void FinishWithGrassHorizontalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(2, 0);
        cell2.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation()) || c.getLocation().equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle with a tent at (0,0)
     * Tests FinishWithGrassDirectRule on GRASS tiles vertical of the tent
     * at (0,1) and (0,2)
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void FinishWithGrassVerticalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(0, 1);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(0, 2);
        cell2.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation()) || c.getLocation().equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle with a tent at (0,0)
     * Tests FinishWithGrassDirectRule on GRASS tiles
     * at (1,0), (2,0), (0,1), and (0,2)
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void FinishWithGrassTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(2, 0);
        cell2.setData(TreeTentType.GRASS);
        TreeTentCell cell3 = board.getCell(0, 1);
        cell3.setData(TreeTentType.GRASS);
        TreeTentCell cell4 = board.getCell(0, 2);
        cell4.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation()) ||
                    c.getLocation().equals(cell2.getLocation()) ||
                    c.getLocation().equals(cell3.getLocation()) ||
                    c.getLocation().equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle with no tents
     * Tests FinishWithGrassDirectRule on GRASS tiles
     * GRASS tiles fill entire board
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void NoTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/NoTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        List<TreeTentCell> cells = new ArrayList<TreeTentCell>();
        for (int i = 0; i < board.getWidth(); i++) {
            for (int k = 0; k < board.getHeight(); k++) {
                TreeTentCell c = board.getCell(i, k);
                c.setData(TreeTentType.GRASS);
                cells.add(c);
            }
        }

        for (TreeTentCell c : cells) {
            board.addModifiedData(c);
        }

        Assert.assertNull(RULE.checkRule(transition));

        for (TreeTentCell c : cells) {
            Assert.assertNull(RULE.checkRuleAt(transition, c));
        }
    }
    /**
     * 3x3 TreeTent puzzle with a tent at (1,1)
     * Tests FinishWithGrassDirectRule on GRASS tiles surrounding the tent
     * at (1,0), (0,1), (2,1), and (1,2)
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void MiddleTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/MiddleTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        TreeTentCell cell2 = board.getCell(0, 1);
        TreeTentCell cell3 = board.getCell(2, 1);
        TreeTentCell cell4 = board.getCell(1, 2);

        cell1.setData(TreeTentType.GRASS);
        cell2.setData(TreeTentType.GRASS);
        cell3.setData(TreeTentType.GRASS);
        cell4.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation()) ||
                    c.getLocation().equals(cell2.getLocation()) ||
                    c.getLocation().equals(cell3.getLocation()) ||
                    c.getLocation().equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle with missing tents
     * Tests FinishWithGrassDirectRule on GRASS tiles filling the puzzle
     * all GRASS tiles should fail the FinishWithGrassDirectRule
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void FailTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/FailTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        List<TreeTentCell> cells = new ArrayList<TreeTentCell>();
        for (int i = 0; i < board.getWidth(); i++) {
            for (int k = 0; k < board.getHeight(); k++) {
                TreeTentCell c = board.getCell(i, k);
                c.setData(TreeTentType.GRASS);
                cells.add(c);
            }
        }

        for (TreeTentCell c : cells) {
            board.addModifiedData(c);
        }

        Assert.assertNotNull(RULE.checkRule(transition));

        for (TreeTentCell c : cells) {
            Assert.assertNotNull(RULE.checkRuleAt(transition, c));
        }
    }
    /**
     * 7x7 TreeTent puzzle with multiple tents spaced out
     * Tests FinishWithGrassDirectRule on GRASS tiles between the tents
     * at (0,3), (2,3), (4,3), and (6,3)
     * 
     * @throws InvalidFileFormatException
     */
    @Test
    public void SpacedOutTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/FinishWithGrassDirectRule/SpacedOutTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell cell1 = board.getCell(0, 3);
        TreeTentCell cell2 = board.getCell(2, 3);
        TreeTentCell cell3 = board.getCell(4, 3);
        TreeTentCell cell4 = board.getCell(6, 3);

        cell1.setData(TreeTentType.GRASS);
        cell2.setData(TreeTentType.GRASS);
        cell3.setData(TreeTentType.GRASS);
        cell4.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);

        Assert.assertNull(RULE.checkRule(transition));

        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation()) ||
                    c.getLocation().equals(cell2.getLocation()) ||
                    c.getLocation().equals(cell3.getLocation()) ||
                    c.getLocation().equals(cell4.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
                else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}