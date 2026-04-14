package puzzles.lightup.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;
import edu.rpi.legup.puzzle.lightup.rules.EmptyCellinLightDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmptyCellinLightDirectRuleTest {
    private static final EmptyCellinLightDirectRule RULE = new EmptyCellinLightDirectRule();
    private static LightUp lightUp;

    @BeforeClass
    public static void setUp() {
        lightUp = new LightUp();
    }

    @Test
    // tests a 3x3 board with with a 0 black tile in the center and lightbulbs in top left and
    // bototm
    // right
    // confirms the rest of the tiles must be empty
    public void EmptyCellinLightDirectRule() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/lightup/rules/EmptyCellinLightDirectRule/EmptyCells", lightUp);
        TreeNode rootNode = lightUp.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        // get board state
        LightUpBoard board = (LightUpBoard) transition.getBoard();

        // change the board's cells considering the emptycellinlight rule
        LightUpCell cell2 = board.getCell(1, 0);
        cell2.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell2);

        LightUpCell cell3 = board.getCell(0, 1);
        cell3.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell3);

        LightUpCell cell4 = board.getCell(2, 0);
        cell4.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell4);

        LightUpCell cell5 = board.getCell(0, 2);
        cell5.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell5);

        LightUpCell cell6 = board.getCell(1, 2);
        cell6.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell6);

        LightUpCell cell7 = board.getCell(2, 1);
        cell7.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell7);

        LightUpCell cell8 = board.getCell(3, 0);
        cell8.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell8);

        LightUpCell cell9 = board.getCell(3, 2);
        cell9.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell9);

        LightUpCell cell10 = board.getCell(2, 3);
        cell10.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell10);

        LightUpCell cell11 = board.getCell(0, 3);
        cell11.setData(LightUpCellType.EMPTY.value);
        board.addModifiedData(cell11);

        // confirm there is a logical following of the EmptyCellinLight rule
        Assert.assertNull(RULE.checkRule(transition));

        // cells (0,0) and (2,2) are not empty because they have lightbulbs, (1,1)
        // because it is a black tile, and (1,3),(3,1),(3,3) because they are not lit. Confirm the
        // rest
        // are empty
        LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 0 && j == 0)
                        || (i == 2 && j == 2)
                        || (i == 1 && j == 1)
                        || (i == 1 && j == 3)
                        || (i == 3 && j == 1)
                        || (i == 3 && j == 3)) {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                } else {
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
    }
}
