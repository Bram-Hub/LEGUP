package puzzles.lightup.rules;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.rules.TooManyBulbsContradictionRule;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.save.InvalidFileFormatException;

import org.junit.BeforeClass;
import org.junit.Test;

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
    public void TooManyBulbsContradictionRule_BlockEnclosed() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooManyBulbsContradictionRule/BlockEnclosed", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
    }

    @Test
    public void TooManyBulbsContradictionRule_CornerBlockEnclosed() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooManybulbsContradictionRule/CornerBlockEnclosed", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
    }
//
    @Test
    public void TooManyBulbsContradictionRule_ManyBlocksEnclosed() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooManyBulbsContradictionRule/ManyBlocksEnclosed", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));

        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }

    @Test
    public void TooManyBulbsContradictionRule_CompleteBoardBlockEnclosed() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/TooManyBulbsContradictionRule/CompleteBoardBlockEnclosed", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));

        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(0, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 2)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 0)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(2, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));
    }
}
