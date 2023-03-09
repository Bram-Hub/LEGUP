package puzzles.lightup.rules;

import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import org.junit.Assert;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.CannotLightACellContradictionRule;
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
    public void CannotLightaCellContradictionRule() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/lightup/rules/CannotLightACellContradictionRule/CannotLightACell", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        //confirm there is a contradiction somewhere on the board
        Assert.assertNull(RULE.checkContradiction(board));

        //confirm it is impossible to light up these squares
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(1, 3)));
        Assert.assertNull(RULE.checkContradictionAt(board, board.getCell(3, 3)));
        
        //confirm these are not required to be lit because they are already lit or unable to be
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(0, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 1)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(1, 0)));
        Assert.assertNotNull(RULE.checkContradictionAt(board, board.getCell(3, 2)));
    }
}
