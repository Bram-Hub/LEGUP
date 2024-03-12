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
            "puzzles/treetent/rules/TentForTreeDirectRule/TentForTreeOneTreeOneTent",
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
     * @throws InvalidFileFormatException Test to check that a line connecting a tree and tent
     *    while there are multiple tents around the tree fails
     */
    @Test
    public void TentForTreeOneTreeTwoTentTest() throws InvalidFileFormatException {

        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/TentForTreeDirectRule/TentForTreeOneTreeTwoTent",
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
            Assert.assertNotNull(RULE.checkRuleAt(transition, l));
        }
    }


}
