package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.TooManyBulbsContradictionRule;
import edu.rpi.legup.model.PuzzleImporter;
import legup.MockGameBoardFacade;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import org.junit.Assert;

public class TooManyBulbsContradictionRuleTest {
    private static final TooManyBulbsContradictionRule RULE = new TooManyBulbsContradictionRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }
    @Test
    public void TooFewBulbsContradictionRule() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooManyBulbsContradictionRule/TooMany", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        //confirm there is a contradiction somewhere on the board
        Assert.assertNull(RULE.checkContradiction(board));

        //confirm that there are too many bulbs around the black tiles
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 4)));

        //confirm there are no requirements for number of bulbs around non-black tiles or 0 tiles
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(5, 5)));
    }
}
