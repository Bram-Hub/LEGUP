package puzzles.battleship.rules;

import org.junit.*;

import legup.MockGameBoardFacade;
import legup.TestUtilities;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.battleship.*;
import edu.rpi.legup.puzzle.battleship.rules.*;
import edu.rpi.legup.save.InvalidFileFormatException;

public class FinishWithShipsBasicRuleTests {
    private static final FinishWithShipsBasicRule RULE
            = new FinishWithShipsBasicRule();

    private static Battleship battleship;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        battleship = new Battleship();
    }

    //@Test
    public void HorizontalValidTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/battleship/rules" +
                        "/FinishWithShipsBasicRuleTests/HorizontalValidBoard",
                battleship);
        TreeNode rootNode = battleship.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkRule(transition));
    }

    //@Test
    public void VerticaValidTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/battleship/rules" +
                        "/FinishWithShipsBasicRuleTests/VerticalValidBoard",
                battleship);
        TreeNode rootNode = battleship.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNull(RULE.checkRule(transition));
    }

    //@Test
    public void HorizontalInvalidTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/battleship/rules" +
                        "/FinishWithShipsBasicRuleTests/HorizontalInvalidBoard",
                battleship);
        TreeNode rootNode = battleship.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkRule(transition));
    }

    //@Test
    public void VerticalInvalidTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/battleship/rules" +
                        "/FinishWithShipsBasicRuleTests/VerticalInvalidBoard",
                battleship);
        TreeNode rootNode = battleship.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        Assert.assertNotNull(RULE.checkRule(transition));
    }
}
