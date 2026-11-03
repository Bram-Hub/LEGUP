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

    /**
     * Tests the Finish With White Direct Rule on a 3x3 board.
     * There is a value square of 0 in the center. All squares, including the value square, are colored white.
     */
    @Test
    public void FinishWithWhiteDirectRuleTest_3x3Center0FillWhiteTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithWhiteDirectRule/3x3Center0FillWhite", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // loop through all cells within the board. i = x, j = y
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                FillapixCell cell = board.getCell(i, j); // set the current cell
                Assert.assertNotNull(cell);

                cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
                board.addModifiedData(cell);
            }
        }

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests the Finish With White Direct Rule on a 3x3 board.
     * There is a value square of 3 in the center. The 1st & 2nd columns are colored white, the 3rd colored black.
     * Color of the 3rd column is preset in 3x3WhiteBlack rule puzzle file
     */
    @Test
    public void FinishWithWhiteDirectRuleTest_3x3WhiteBlackMixTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithWhiteDirectRule/3x3WhiteBlackMix", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // loop through all cells within the board. i = x, j = y
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                FillapixCell cell = board.getCell(i, j); // set the current cell
                Assert.assertNotNull(cell);

                // only color white the 3rd (last) column
                if ( i != 2 ) {
                    cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
                    board.addModifiedData(cell);
                }

            }
        }

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    

}
