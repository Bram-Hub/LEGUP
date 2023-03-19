package puzzles.lightup.rules;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.rules.CannotLightACellContradictionRule;
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

public class CannotLightACellContradictionRuleTest {
    private static final CannotLightACellContradictionRule RULE = new CannotLightACellContradictionRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    public void CannotLightACellContradictionRule_CannotFillMiddle() throws InvalidFileFormatException  {
        TestUtilities.importTestBoard("puzzles/lightup/rules/CannotLightACellContradictionRule/CannotFillMiddle", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Assert.assertNull(RULE.checkContradiction(board));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(2, 2)));

        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
    }
}
