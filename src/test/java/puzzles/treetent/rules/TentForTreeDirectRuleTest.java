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

  /***
   * @throws InvalidFileFormatException Test to check that a line connecting a tree and tent
   *    that are only adjacent to each other is valid
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

    /***
     * @throws InvalidFileFormatException Test to check that a line connecting a tent to a tree
     *    while there are multiple trees around the tent works
     *
     *    <p>3x3 board with trees at (1, 0) and (1, 2) and a tent at (1, 1). Creating a line
     *    from (1, 0) to (1, 1) works because there is only one tent adjacent to the tree
     *    at (1, 0)
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

    /***
     * @throws InvalidFileFormatException Test to check if attempting to connect a tree to
     *  an already connected tent fails
     *
     *  <p>3x3 Board with Trees at (1, 0) and (1, 2) and a Tent at (1, 1)
     *  A Preexisting line connects the tent at (1, 0) and the central tent
     *  Therefore, the tree at (1, 2) does not have a valid tent adjacent to it
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

    /***
     * @throws InvalidFileFormatException Test to check if attempting to connect a tree to
     *  a tent when there are two possibilities fails
     *
     *  <p>3x3 Board with Tree at (1, 1) and Tents at (1, 0) and (1, 2)
     *  The central tree has two possible tents to connect to, so the rule should fail
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
