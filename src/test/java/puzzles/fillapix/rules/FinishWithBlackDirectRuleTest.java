package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.rules.FinishWithBlackDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithBlackDirectRuleTest {
    private static final FinishWithBlackDirectRule RULE = new FinishWithBlackDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    /**
     * Tests the Finish With Black Direct Rule on a 1x1 board.
     * The only square is a value square of 1. It is colored black.
     */
    @Test
    public void FinishWithBlackDirectRuleTest_1x1Center1FillBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/1x1Center1FillBlack", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // go through the 1 cell of the board
        FillapixCell cell = board.getCell(0, 0); // set the current cell
        Assert.assertNotNull(cell);

        cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
        board.addModifiedData(cell);

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests the Finish With Black Direct Rule on a 3x3 board.
     * There is a value square of 9 in the center. All squares, including the value square, are colored black.
     */
    @Test
    public void FinishWithBlackDirectRuleTest_3x3Center9FillBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/3x3Center9FillBlack", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board
        // loop through all cells within the board. i = x, j = y
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                FillapixCell cell = board.getCell(i, j); // set the current cell
                Assert.assertNotNull(cell);

                cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
                board.addModifiedData(cell);
            }
        }

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests the Finish With Black Direct Rule on a 2x2 board.
     * There are 2 value squares: A 4-value in the top left and 4-value in the bottom right.
     * All squares are colored black.
     */
    @Test
    public void FinishWithBlackDirectRuleTest_2x2MultiNumberTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/2x2MultiNumber", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board
        // loop through all cells within the board. i = x, j = y
        for ( int i = 0; i < 2; i++ ) {
            for ( int j = 0; j < 2; j++ ) {
                FillapixCell cell = board.getCell(i, j); // set the current cell
                Assert.assertNotNull(cell);

                cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
                board.addModifiedData(cell);
            }
        }

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests a false application of the Finish With Black Direct Rule on a 3x3 board.
     * There is a value square of 6 in the center. The Middle and Right columns are colored black, and the
     * Left column is colored white.
     */
    @Test
    public void FinishWithBlackDirectRuleTest_3x3FalseWhiteBlackTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/3x3FalseWhiteBlack", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board
        // loop through all cells within the board. i = x, j = y
        for ( int i = 0; i < 3; i++ ) {
            for ( int j = 0; j < 3; j++ ) {
                FillapixCell cell = board.getCell(i, j); // set the current cell
                Assert.assertNotNull(cell);

                // set everything but first column to black
                if ( i == 0 ) {
                    cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
                }
                else {
                    cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
                }

                board.addModifiedData(cell);
            }
        }

        // Test once gone through entire board
        Assert.assertNotNull(RULE.checkRule(transition));
    }

    /**
     * Tests a false application of the Finish With Black Direct Rule on a 1x3 board.
     * There is a value square of 2 in the center. All 3 squares are colored black.
     */
    @Test
    public void FinishWithBlackDirectRuleTest_1x3FalseOverflowTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/FinishWithBlackDirectRule/1x3FalseOverflow", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board
        // loop through the row of the board. i = x
        for ( int i = 0; i < 3; i++ ) {
            FillapixCell cell = board.getCell(i, 0); // set the current cell
            Assert.assertNotNull(cell);

            cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
            board.addModifiedData(cell);
        }

        // Test once gone through entire board
        Assert.assertNotNull(RULE.checkRule(transition));
    }
}