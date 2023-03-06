package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.TooFewBulbsContradictionRule;
import edu.rpi.legup.model.PuzzleImporter;
import legup.MockGameBoardFacade;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import org.junit.Assert;

public class TooFewBulbsContradictionRuleTest {
    private static final TooFewBulbsContradictionRule RULE = new TooFewBulbsContradictionRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    public void TooFewBulbsContradictionRule_LightInHorizontalPath() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooFewBulbsContradictionRule/BulbsAroundBlackTile", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));

        //confirm that there arent enough bulbs around the black tiles
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(4, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 4)));

        //confirm there are no requirements for number of bulbs around non-black tiles or 0 tiles
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(4, 4)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(5, 5)));
        
        //intentional failure for sanity check
        //Assert.assertEquals(2, 3);
    }
}
