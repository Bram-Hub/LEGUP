package puzzles.lightup.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import edu.rpi.legup.puzzle.lightup.rules.MustLightDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MustLightDirectRuleTest {
    private static final MustLightDirectRule RULE = new MustLightDirectRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    public void MustLightTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/lightup/rules/MustLightDirectRule/MustLight", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        // change the board's cells considering the MustLight rule
        LightUpCell cell1 = board.getCell(1, 2);
        cell1.setData(LightUpCellType.BULB.value);
        board.addModifiedData(cell1);

        // confirm there is a logical following of the FinishWithBulbs rule
        Assert.assertNull(RULE.checkRule(transition));

        // only the cell above should change following the rule
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if (i == 2 && j == 1) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
