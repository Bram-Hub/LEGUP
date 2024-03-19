package puzzles.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.*;
import edu.rpi.legup.puzzle.treetent.rules.LinkTentCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class LinkTentCaseRuleTest {
    private static final LinkTentCaseRule RULE = new LinkTentCaseRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * empty 3x3 TreeTent puzzle Tests FillinRowCaseRule on row with 3 UNKNOWN tiles
     * and a clue of 0 tents in the row.
     *
     * <p>checks that 1 case is created and that it is equivalent to FinishWithGrass rule
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void LinkTentFourTreesTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/LinkTentCaseRule/FourTreesOneTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell test_location = board.getCell(1, 1);
        ArrayList<Board> cases = RULE.getCases(board, test_location);

        // assert that four cases were found
        Assert.assertEquals(4, cases.size());

        ArrayList<TreeTentLine> expectedLines = new ArrayList<>();
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(1, 0)));
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(0, 1)));
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(2, 1)));
        expectedLines.addFirst(new TreeTentLine(board.getCell(1, 1), board.getCell(1, 2)));

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard ;
            ArrayList<TreeTentLine> lines = testCase.getLines();

            // Each case should connect one line from the tent to
            // one of the four trees next to it
            Assert.assertEquals(1, lines.size());
            TreeTentLine line = lines.getFirst();

            // Check to make sure that cases do not repeat
            // and cover all four possibilities
            boolean exists = false;
            for (TreeTentLine expectedLine : expectedLines) {
                if (line.compare(line)) {
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
}
