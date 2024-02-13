package puzzles.skyscrapers.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.Skyscrapers;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.rules.ExceedingVisibilityContradictionRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ExceedingVisibilityContradictionTest {

  private static final ExceedingVisibilityContradictionRule RULE =
      new ExceedingVisibilityContradictionRule();
  private static Skyscrapers skyscrapers;

  @BeforeClass
  public static void setUp() {
    MockGameBoardFacade.getInstance();
    skyscrapers = new Skyscrapers();
  }

  // empty
  @Test
  public void ExceedingVisibilityContradictionRule_EmptyBoardTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/empty", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
    }
  }

  // correct board, no cont
  @Test
  public void ExceedingVisibilityContradictionRule_SolvedBoardTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard("puzzles/skyscrapers/rules/common/Solved", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
    }
  }

  // invalid board, no cont
  @Test
  public void ExceedingVisibilityContradictionRule_OtherContradictionTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/skyscrapers/rules/DuplicateNumberContradictionRule/RowContradiction", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNotNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
    }
  }

  // on row
  @Test
  public void ExceedingVisibilityContradictionRule_RowContradictionTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/skyscrapers/rules/VisibilityContradictionRules/FullRowContradiction", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      if (i == 1 || i == 3) {
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
      } else {
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
      }
    }
  }

  // on col
  @Test
  public void ExceedingVisibilityContradictionRule_ColContradictionTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/skyscrapers/rules/VisibilityContradictionRules/FullColContradiction", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      if (i == 2 || i == 3) {
        Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
      } else {
        Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
      }
    }
  }

  // multitudes
  @Test
  public void ExceedingVisibilityContradictionRule_AllContradictionTest()
      throws InvalidFileFormatException {
    TestUtilities.importTestBoard(
        "puzzles/skyscrapers/rules/VisibilityContradictionRules/AllContradiction", skyscrapers);

    TreeNode rootNode = skyscrapers.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    Assert.assertNull(RULE.checkContradiction((SkyscrapersBoard) transition.getBoard()));

    SkyscrapersBoard board = (SkyscrapersBoard) transition.getBoard();
    for (int i = 0; i < board.getHeight(); i++) {
      Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(i, i)));
    }
  }
}
