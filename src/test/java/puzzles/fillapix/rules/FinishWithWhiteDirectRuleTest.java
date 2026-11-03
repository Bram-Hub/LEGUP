package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.rules.FinishWithWhiteDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithWhiteDirectRuleTest {

    private static final FinishWithWhiteDirectRule RULE = new FinishWithWhiteDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    /**
     * Tests the Finish With White Direct Rule on a 1x1 board.
     * The only square is a value square of 0. It is colored white.
     */
    @Test
    public void FinishWithWhiteDirectRuleTest_1x1Center0FillWhiteTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithWhiteDirectRule/1x1Center0FillWhite", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // go through the 1 cell of the board
        FillapixCell cell = board.getCell(0, 0); // set the current cell
        Assert.assertNotNull(cell);

        cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
        board.addModifiedData(cell);

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

}
