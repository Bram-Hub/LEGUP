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

    /**
     * 3x3 TreeTent puzzle Tests TreeForTentDirectRule
     * <p> TENT at (1, 0); TREE at (1, 1)
     * XTX
     * XRX
     * XXX
     * <p> Makes a line between (1, 0) and (1, 1)
     * Checks that the rule correctly detects the central tree as the only possible connection
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

    /**
     * 3x3 TreeTent puzzle Tests TreeForTentDirectRule
     * <p> TENT at (1, 0) and (1, 2); TREE at (1, 1)
     * XTX
     * XRX
     * XTX
     * <p> Makes a line between (1, 0) and (1, 1)
     * Checks that the rule works when connecting a line between the tent at (1, 0) and the tree at (1, 1)
     */
    @Test
    public void TentForTreeWithArbitraryTreeTest() throws InvalidFileFormatException {

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

    /**
     * 3x3 TreeTent puzzle Tests TreeForTentDirectRule
     * <p> TENT at (1, 0) and (1, 2); TREE at (1, 1); LINE between (1, 0) and (1, 1)
     * XTX
     * XRX
     * XTX
     * <p> Makes a line between (1, 1) and (1, 2)
     * Checks that the rule fails for the tent at (1, 2) because there are no valid trees to connect to
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

    /**
     * 3x3 TreeTent puzzle Tests TreeForTentDirectRule
     * <p> TENT at (1, 1); TREE at (1, 0) and (1, 2)
     * XRX
     * XTX
     * XRX
     * <p> Makes a line between (1, 1) and (1, 2)
     * Checks that the rule fails for the tree at (1, 1) because there are two valid trees to connect to
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
