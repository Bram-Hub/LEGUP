package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import edu.rpi.legup.puzzle.fillapix.rules.TouchingCornersDirectRule;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;

import edu.rpi.legup.save.InvalidFileFormatException;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TouchingCornersDirectRuleTest {
    private static final TouchingCornersDirectRule RULE =
            new TouchingCornersDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        fillapix = new Fillapix();
    }

    @Test
    public void TouchingCornersTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/TouchingCornersDirectRule/TouchingCorners", fillapix);
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);
        // get board state
        FillapixBoard board = (FillapixBoard) transition.getBoard();

        // change the board's cells considering the TouchingCorners rule
        //(black cells in the corners around a specific target cell)
        FillapixCell cell1 = board.getCell(0, 0);
        cell1.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell1);

        FillapixCell cell2 = board.getCell(0, 1);
        cell2.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell2);

        FillapixCell cell3 = board.getCell(0, 2);
        cell3.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell3);

        FillapixCell cell4 = board.getCell(1, 0);
        cell4.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell4);

        FillapixCell cell5 = board.getCell(2, 0);
        cell5.setData(FillapixCellType.BLACK.value);
        board.addModifiedData(cell5);

        // confirm there is a logical following of the TouchingCorners rule
        Assert.assertNull(RULE.checkRule(transition));

        // check each square except the center
        /*FillapixCell c;
        for (int i = 0; i < board.getHeight(); i++) {
            for (int j = 0; j < board.getWidth(); j++) {
                c = board.getCell(j, i);
                if ((i == 1 || i == 3) && (j == 1 || j == 3)) {
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
