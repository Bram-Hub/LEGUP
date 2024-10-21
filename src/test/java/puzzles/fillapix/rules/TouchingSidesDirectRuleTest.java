package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.rules.TouchingSidesDirectRule;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TouchingSidesDirectRuleTest {
    private static final TouchingSidesDirectRule RULE =
            new TouchingSidesDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        fillapix = new Fillapix();
    }

    @Test
    public void TouchingSidesTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/TouchingSidesDirectRule/TouchingSides", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);
        // get board state
        FillapixBoard board = (FillapixBoard) transition.getBoard();

        // change the board's cells considering the TouchingSides rule
        //(3 black cells to the left of 6 and 3 white cells to the right of 3)
        FillapixCell cell1 = board.getCell(0, 1);
        cell1.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell1);

        FillapixCell cell2 = board.getCell(0, 2);
        cell2.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell2);

        FillapixCell cell3 = board.getCell(0, 3);
        cell3.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell3);

        FillapixCell cell4 = board.getCell(3, 1);
        cell4.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell4);

        FillapixCell cell5 = board.getCell(3, 2);
        cell5.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell5);

        FillapixCell cell6 = board.getCell(3, 3);
        cell6.setData(FillapixCellType.WHITE.value);
        board.addModifiedData(cell6);

        // confirm there is a logical following of the TouchingSides rule
        Assert.assertNull(RULE.checkRule(transition));

        // check every square except the top center (2,0)
        /*LightUpCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if (i == 0 && j == 1) {
                    // logically follows
                    Assert.assertNull(RULE.checkRuleAt(transition, c));
                } else {
                    // does not use the rule to logically follow
                    Assert.assertNotNull(RULE.checkRuleAt(transition, c));
                }
            }
        }
         */

    }
}
