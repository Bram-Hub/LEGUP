package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.*;
import edu.rpi.legup.puzzle.treetent.rules.TentForTreeDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class TentForTreeDirectRuleTest {

    private static final TentForTreeDirectRule RULE = new TentForTreeDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

  /**
   * 3x3 TreeTent puzzle Tests TentForTreeDirectRule
   * <p> TREE at (1, 0); TENT at (1, 1)
   * XRX
   * XTX
   * XXX
   * <p> Makes a line between (1, 0) and (1, 1)
   * Checks that the rule correctly detects the central tent as the only possible connection
   */
  @Test
  public void TentForTreeTestOneTreeOneTentTest() throws InvalidFileFormatException {

    TestUtilities.importTestBoard(
            "puzzles/treetent/rules/TentForTreeDirectRule/OneTreeOneTent",
          treetent);

    TreeNode rootNode = treetent.getTree().getRootNode();
    TreeTransition transition = rootNode.getChildren().get(0);
    transition.setRule(RULE);

    TreeTentBoard board = (TreeTentBoard) transition.getBoard();

    TreeTentCell cell1 = board.getCell(1, 0);
    TreeTentCell cell2 = board.getCell(1, 1);
    TreeTentLine line = new TreeTentLine(cell1, cell2);

    board.addModifiedData(line);
    board.getLines().add(line);

    Assert.assertNull(RULE.checkRule(transition));

    ArrayList<TreeTentLine> lines = board.getLines();
    for (TreeTentLine l : lines) {
        if (l.compare((line))) {
            Assert.assertNull(RULE.checkRuleAt(transition, l));
        } else {
            Assert.assertNotNull(RULE.checkRuleAt(transition, l));
        }
    }
  }

  /**
   * 3x3 TreeTent puzzle Tests TentForTreeDirectRule
   * <p> TREE at (1, 0) and (1, 2); TENT at (1, 1)
   * XRX
   * XTX
   * XRX
   * <p> Makes a line between (1, 0) and (1, 1)
   * Checks that the rule works when connecting a line between the tree at (1, 0) and tent at (1, 1)
   */
  @Test
  public void TentForTreeArbitraryTreeTest() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TentForTreeDirectRule/ArbitraryTree",
                treetent);

        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 0);
        TreeTentCell cell2 = board.getCell(1, 1);
        TreeTentLine line = new TreeTentLine(cell1, cell2);

        board.addModifiedData(line);
        board.getLines().add(line);

        Assert.assertNull(RULE.checkRule(transition));
    }

  /**
   * 3x3 TreeTent puzzle Tests TentForTreeDirectRule
   * <p> TREE at (1, 0) and (1, 2); TENT at (1, 1); LINE between (1, 0) and (1, 1)
   * XRX
   * XTX
   * XRX
   * <p> Makes a line between (1, 1) and (1, 2)
   * Checks that the rule fails for the tree at (1, 2) because there are no valid tents to connect to
   */
  @Test
  public void TentForTreeConnectedTent() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TentForTreeDirectRule/ArbitraryTree",
                treetent);

        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 2);
        TreeTentCell cell2 = board.getCell(1, 1);
        TreeTentLine line = new TreeTentLine(cell1, cell2);

        board.addModifiedData(line);
        board.getLines().add(line);

        Assert.assertNull(RULE.checkRule(transition));

        ArrayList<TreeTentLine> lines = board.getLines();
        for (TreeTentLine l : lines) {
            Assert.assertNotNull(RULE.checkRuleAt(transition, l));
        }
    }

  /**
   * 3x3 TreeTent puzzle Tests TentForTreeDirectRule
   * <p> TREE at (1, 1); TENT at (1, 0) and (1, 2)
   * XTX
   * XRX
   * XTX
   * <p> Makes a line between (1, 1) and (1, 2)
   * Checks that the rule fails for the tree at (1, 1) because there are two valid tents to connect to
   */
  @Test
  public void TentForTreeOneTreeTwoAdjacentTent() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TentForTreeDirectRule/OneTreeTwoAdjacentTent",
                treetent);

        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        TreeTentCell cell1 = board.getCell(1, 2);
        TreeTentCell cell2 = board.getCell(1, 1);
        TreeTentLine line = new TreeTentLine(cell1, cell2);

        board.addModifiedData(line);
        board.getLines().add(line);

        Assert.assertNotNull(RULE.checkRule(transition));
    }
}
