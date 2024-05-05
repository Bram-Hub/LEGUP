package puzzles.treetent.rules;

import com.sun.source.doctree.LinkTree;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.rules.LinkTreeCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

public class LinkTreeCaseRuleTest {
  private static final LinkTreeCaseRule RULE = new LinkTreeCaseRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * empty 3x3 TreeTent puzzle Tests LinkTentCaseRule on a central tree
     * with one tent above
     *
     * <p> Ensures one case is created that connects the tree to the tent.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void LinkTentOneTentTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LinkTreeCaseRule/OneTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell test_location = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, test_location);

        // assert that no cases were found
        Assert.assertEquals(1, cases.size());
        TreeTentBoard testCase = (TreeTentBoard) cases.getFirst();

        TreeTentLine expectedLine = new TreeTentLine(board.getCell(1, 1), board.getCell(1, 0));

        ArrayList<TreeTentLine> lines = testCase.getLines();

        // One line connecting the tree to the tent
        Assert.assertEquals(1, lines.size());
        TreeTentLine line = lines.getFirst();

        // Expected line
        Assert.assertTrue(line.compare(expectedLine));

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                original_cell = board.getCell(w, h);
                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }
    }

    /**
     * empty 3x3 TreeTent puzzle Tests LinkTentCaseRule on a central tree
     * with two tents, one on the left and one on the right.
     *
     * <p> Ensures two cases are created, one connecting the tree and the
     * left tent, and one connecting the tree and the right tent.
     * Because tents must be surrounded by grass, there can be at most
     * two tents around a given tree.
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void LinkTentTwoTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LinkTreeCaseRule/TwoTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell test_location = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, test_location);

        // assert that no cases were found
        Assert.assertEquals(2, cases.size());

        ArrayList<TreeTentLine> expectedLines = new ArrayList<>();
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(0, 1)));
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(2, 1)));

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard ;
            ArrayList<TreeTentLine> lines = testCase.getLines();

            // Each case should connect one line from the tent to
            // either the left or right tree
            Assert.assertEquals(1, lines.size());
            TreeTentLine line = lines.getFirst();

            // Check to make sure that cases do not repeat
            // and cover both possibilities
            boolean exists = false;
            for (TreeTentLine expectedLine : expectedLines) {
                if (line.compare(expectedLine)) {
                    expectedLines.remove(expectedLine);
                    exists = true;
                    break;
                }
            }

            Assert.assertTrue(exists);

            // checks other cells have not been modified
            TreeTentCell original_cell;
            TreeTentCell case_cell;

            for (int w = 0; w < board.getWidth(); w++) {
                for (int h = 0; h < board.getHeight(); h++) {
                    original_cell = board.getCell(w, h);
                    case_cell = testCase.getCell(w, h);
                    Assert.assertEquals(original_cell.getType(), case_cell.getType());
                }
            }
        }
    }

    /**
     * empty 3x3 TreeTent puzzle Tests LinkTentCaseRule on a central tree
     * with zero tents around it.
     *
     * <p> Ensures no cases are created
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void LinkTentNoTreesTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LinkTreeCaseRule/NoTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell test_location = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, test_location);

        // assert that no cases were found
        Assert.assertEquals(0, cases.size());
    }

    /**
     * empty 3x3 TreeTent puzzle Tests LinkTentCaseRule on a central tree
     * with tents on a diagonal.
     *
     * <p> Ensures no cases are created
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void LinkTentDiagTentsTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LinkTreeCaseRule/NoTents", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell test_location = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, test_location);

        // assert that no cases were found
        Assert.assertEquals(0, cases.size());
    }
}
