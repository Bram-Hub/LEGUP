package puzzles.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTent;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentType;
import edu.rpi.legup.puzzle.treetent.rules.FillinRowCaseRule;
import edu.rpi.legup.puzzle.treetent.rules.TentOrGrassCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class TentOrGrassCaseRuleTest {
    private static final TentOrGrassCaseRule RULE = new TentOrGrassCaseRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp(){
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * @throws InvalidFileFormatException
     * A temporary test
     */
    @Test
    public void TentOrGrassCaseRule_Test() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/treetent/rules/TentOrGrassCaseRule/TentOrGrassTest", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentCell testing_cell = board.getCell(1, 0);
        ArrayList<Board> cases = RULE.getCases(board, testing_cell);
        // assert correct number of cases created
        Assert.assertEquals(2, cases.size());
        //Store the 0,1 cells from each case
        //Assert that the Array of their states match to a an array of the expected.

    }

}
