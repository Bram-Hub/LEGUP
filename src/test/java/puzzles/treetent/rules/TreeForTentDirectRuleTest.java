package puzzles.treetent.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.rules.TreeForTentDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class TreeForTentDirectRuleTest {

    private static final TreeForTentDirectRule RULE = new TreeForTentDirectRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

  /***
   * @throws InvalidFileFormatException Test to check that a line connecting a tree and tent
   *    that are only adjacent to each other is valid
   *
   *    <p>3x3 Board with Tree at (1, 0) and a Tent at (1, 1)
   */
  @Test
  public void TreeForTentTestOneTreeOneTentTest() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TreeForTentDirectRule/OneTentOneTree",
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
     * @throws InvalidFileFormatException Test to check that a line connecting a tree to a tent
     *    while there are multiple tents around the tree works
     *
     *    <p>3x3 board with tents at (1, 0) and (1, 2) and a tree at (1, 1). Creating a line
     *    from (1, 0) to (1, 1) works because there is only one tree adjacent to the tent
     *    at (1, 0)
     */
    @Test
    public void TentForTreeArbitraryTreeTest() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TreeForTentDirectRule/ArbitraryTent",
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
     * @throws InvalidFileFormatException Test to check if attempting to connect a tent to
     *  an already connected tree fails
     *
     *  <p>3x3 Board with Tents at (1, 0) and (1, 2) and a Tree at (1, 1)
     *  A Preexisting line connects the tent at (1, 0) and the central tree
     *  Therefore, the tent at (1, 2) does not have a valid tree adjacent to it
     */
    @Test
    public void TentForTreeConnectedTent() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TreeForTentDirectRule/ConnectedTree",
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
     * @throws InvalidFileFormatException Test to check if attempting to connect a tent to
     *  a tree when there are two trees fails
     *
     *  <p>3x3 Board with Tent at (1, 1) and Trees at (1, 0) and (1, 2)
     *  The central tent has two possible trees to connect to, so the rule should fail
     */
    @Test
    public void TentForTreeOneTreeTwoAdjacentTent() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TreeForTentDirectRule/OneTentTwoAdjacentTrees",
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
