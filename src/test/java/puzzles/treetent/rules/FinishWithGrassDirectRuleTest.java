package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.FinishWithGrassDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithGrassDirectRuleTest {

    private static final FinishWithGrassDirectRule RULE = new FinishWithGrassDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

  /**
   * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Tent at (1, 1)
   * XXX x
   * GTG 1
   * XXX x
   * xxx
   * <p> Makes (0, 1) and (2, 1) GRASS
   * Checks if the rule detects the middle row to be filled in correctly
   */
  @Test
  public void FinishWithGrassHorizontalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
        TreeTentCell cell1 = board.getCell(1, 0);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(2, 0);
        cell2.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())
                        || c.getLocation().equals(cell2.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

  /**
   * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Tent at (0, 0)
   * TXX x
   * GXX x
   * GXX x
   * 1xx
   * <p> Makes (0, 1) and (0, 2) GRASS
   * Checks if the rule detects the leftmost column to be filled in correctly
   */
  @Test
  public void FinishWithGrassVerticalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
        TreeTentCell cell1 = board.getCell(0, 1);
        cell1.setData(TreeTentType.GRASS);
        TreeTentCell cell2 = board.getCell(0, 2);
        cell2.setData(TreeTentType.GRASS);

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())
                        || c.getLocation().equals(cell2.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

    /**
     * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
     * <p> Tent at (0, 0)
     * TGG 1
     * GXX x
     * GXX x
     * 1xx
     * <p> Makes (0, 1), (0, 2), (1, 0), and (2, 0) GRASS
     * Checks if the rule detects the top row and leftmost column to be filled in correctly
     */
    @Test
    public void FinishWithGrassTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/CornerTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
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

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())
                        || c.getLocation().equals(cell2.getLocation())
                        || c.getLocation().equals(cell3.getLocation())
                        || c.getLocation().equals(cell4.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

  /**
   * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Empty
   * GGG 0
   * GGG 0
   * GGG 0
   * 000
   * <p> Fill Board with GRASS
   * Checks if the rule allows all cells to be filled when the clue for all rows and columns is zero.
   */
  @Test
  public void NoTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/NoTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
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

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // all cells should change following the rule
        for (TreeTentCell c : cells) {
            // logically follows
            Assert.assertNull(RULE.checkRuleAt(transition, c));
        }
    }

  /**
   * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Tent at (1, 1)
   * XGX x
   * GTG 1
   * XGX x
   * x1x
   * <p> Makes (1, 0), (0, 1), (2, 1), and (1, 2) GRASS
   * Checks if the rule correctly allows the central row and column to be filled with grass.
   */
  @Test
  public void MiddleTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/MiddleTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
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

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())
                        || c.getLocation().equals(cell2.getLocation())
                        || c.getLocation().equals(cell3.getLocation())
                        || c.getLocation().equals(cell4.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }

  /**
   * 3x3 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Empty
   * GGG 1
   * GGG 1
   * GGG 1
   * 111
   * <p> Fill Board with GRASS
   * Checks if the rule is not valid when a row or column does not have the required number of tents but is filled with grass
   */
  @Test
  public void FailTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/FailTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells not following the FinishWithGrass rule
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

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNotNull(RULE.checkRule(transition));

        // all cells should fail the rule test
        for (TreeTentCell c : cells) {
            // does not use the rule to logically follow
            Assert.assertNotNull(RULE.checkRuleAt(transition, c));
        }
    }

  /**
   * 7x7 TreeTent puzzle Tests FinishWithGrassDirectRule
   * <p> Tents at (1, 3), (3, 3), and (5, 3)
   * XXXXXXX x
   * XXXXXXX x
   * XXXXXXX x
   * TGTGTGT 4
   * XXXXXXX x
   * XXXXXXX x
   * XXXXXXX x
   * xxxxxxx
   * <p> Makes (0, 3), (2, 3), (4, 3), and (6, 3) GRASS
   * Checks if applying the rule on row 3 is valid
   */
  @Test
  public void SpacedOutTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FinishWithGrassDirectRule/SpacedOutTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // change the board's cells considering the FinishWithGrass rule
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

        // confirm there is a logical following of the EmptyField rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        TreeTentCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                c = board.getCell(k, i);
                if (c.getLocation().equals(cell1.getLocation())
                        || c.getLocation().equals(cell2.getLocation())
                        || c.getLocation().equals(cell3.getLocation())
                        || c.getLocation().equals(cell4.getLocation())) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
