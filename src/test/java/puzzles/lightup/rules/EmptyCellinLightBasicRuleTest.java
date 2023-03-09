package puzzles.lightup.rules;

import org.junit.BeforeClass;
import org.junit.Test;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.rules.EmptyCellinLightBasicRule;
import edu.rpi.legup.model.PuzzleImporter;
import legup.MockGameBoardFacade;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import java.util.*;
import org.junit.Assert;

public class EmptyCellinLightBasicRuleTest {
    private static final EmptyCellinLightBasicRule RULE = new EmptyCellinLightBasicRule();
    private static LightUp lightUp;
    private static PuzzleImporter importer;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        lightUp = new LightUp();
        importer = lightUp.getImporter();
    }

    @Test
    public void EmptyCellinLightBasicRule() throws InvalidFileFormatException{
        TestUtilities.importTestBoard("puzzles/lightup/rules/EmptyCellinLightBasicRule/EmptyCellinLight", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE); 

        LightUpBoard board = (LightUpBoard) transition.getBoard();
        Set<PuzzleElement> data = board.getModifiedData();        
        
        Assert.assertNull(RULE.checkRule(transition));

    }
}
