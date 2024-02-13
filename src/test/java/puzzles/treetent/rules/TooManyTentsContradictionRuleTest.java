package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.rules.TooManyTentsContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TooManyTentsContradictionRuleTest {

  private static final TooManyTentsContradictionRule RULE = new TooManyTentsContradictionRule();
  private static TreeTent treetent;

  @BeforeClass
  public static void setUp() {
    MockGameBoardFacade.getInstance();
    treetent = new TreeTent();
  }

  /*
  TESTING BASIS:
  All test in this Rule use a 3x3 table.
  There is a Tree at (1,1)
  There are tents at (0,1) and (2,2)
  All Tent Counts are listed left to right or top to bottom
   */

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if: Row Tent Counts: 0,0,0 Column
   *     Tent Counts: 0,0,0
   */
  @Test
  public void TooManyTentsContradictionRule_TotalFail() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsTotalFail", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 1);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if: Row Tent Counts: 1,0,0 Column
   *     Tent Counts: 0,0,0
   */
  @Test
  public void TooManyTentsContradictionRule_TopRight() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsTopRight", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 0);
    TreeTentCell cell2 = board.getCell(0, 1);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTests if: Row Tent Counts: 0,0,1 Column
   *     Tent Counts: 0,0,0
   */
  @Test
  public void TooManyTentsContradictionRule_BottomRight() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsBottomRight", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 1);
    TreeTentCell cell2 = board.getCell(0, 2);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if: Row Tent Counts: 0,0,0 Column
   *     Tent Counts: 0,1,0
   */
  @Test
  public void TooManyTentsContradictionRule_TopDown() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsTopDown", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 1);
    TreeTentCell cell2 = board.getCell(1, 1);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if: Row Tent Counts: 0,0,0 Column
   *     Tent Counts: 0,0,1
   */
  @Test
  public void TooManyTentsContradictionRule_BottomDown() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsBottomDown", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 1);
    TreeTentCell cell2 = board.getCell(2, 1);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if the Top Tent is completely
   *     accounted for, but not the bottom Row Tent Counts: 1,0,0 Column Tent Counts: 0,1,0
   */
  @Test
  public void TooManyTentsContradictionRule_TopAccount() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsTopAccount", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 0);
    TreeTentCell cell2 = board.getCell(1, 0);
    TreeTentCell cell3 = board.getCell(0, 1);
    TreeTentCell cell4 = board.getCell(1, 1);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation())
            || point.equals(cell2.getLocation())
            || point.equals(cell3.getLocation())
            || point.equals(cell4.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }

  /**
   * @throws InvalidFileFormatException Tests for TooManyTents if the Bottom Tent is completely
   *     accounted for, but not the Top Row Tent Counts: 0,0,1 Column Tent Counts: 0,0,1
   */
  @Test
  public void TooManyTentsContradictionRule_BottomAccount() throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/treetent/rules/TooManyTentsContradictionRule/TooManyTentsBottomAccount", treetent);
    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(0, 1);
    TreeTentCell cell2 = board.getCell(2, 1);
    TreeTentCell cell3 = board.getCell(0, 2);
    TreeTentCell cell4 = board.getCell(2, 2);

    Assert.assertNull(RULE.checkContradiction(board));

    for (int i = 0; i < board.getHeight(); i++) {
      for (int k = 0; k < board.getWidth(); k++) {
        Point point = new Point(k, i);
        if (point.equals(cell1.getLocation())
            || point.equals(cell2.getLocation())
            || point.equals(cell3.getLocation())
            || point.equals(cell4.getLocation())) {
          Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        } else {
          Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
        }
      }
    }
  }
}
