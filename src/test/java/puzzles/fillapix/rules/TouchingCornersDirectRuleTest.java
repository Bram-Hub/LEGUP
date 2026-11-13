package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.FillapixCell;
import edu.rpi.legup.puzzle.fillapix.FillapixCellType;
import edu.rpi.legup.puzzle.fillapix.rules.TouchingCornersDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TouchingCornersDirectRuleTest {

    private static final TouchingCornersDirectRule RULE = new TouchingCornersDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        fillapix = new Fillapix();
    }

    /**
     * Tests the Touching Corners Direct Rule on a 4x4 board.
     * There is a value tile of 6 in the top-right middle, and a value tile of 7 in the bottom-left middle.
     * The 6-tile has 4 black tiles in range, the 7-tile has 4 black tiles in range.
     * The 7-tile needs to fill the space with a black square and the 6-tile fills its own with white.
     */
    @Test
    public void TouchingCornersDirectRuleTest_4x4Differ7Up1 () throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/TouchingCornersDirectRule/4x4Differ7Up1", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // Color (0,2) WHITE
        FillapixCell cell = board.getCell(0, 2); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
        board.addModifiedData(cell);

        // Color (3,3) BLACK
        cell = board.getCell(3, 3); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
        board.addModifiedData(cell);

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests the Touching Corners Direct Rule on a 4x4 board.
     * There is a value tile of 6 in the top-right middle, and a value tile of 7 in the bottom-left middle.
     * The 6-tile has 2 black tiles in range, the 7-tile has 4 black tiles in range.
     * The 7-tile needs to fill the space with a white square and the 6-tile fills its own with black.
     */
    @Test
    public void TouchingCornersDirectRuleTest_4x4Differ6Up1 () throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/TouchingCornersDirectRule/4x4Differ6Up1", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // Color (0,2) BLACK
        FillapixCell cell = board.getCell(0, 2); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
        board.addModifiedData(cell);

        // Color (3,3) WHITE
        cell = board.getCell(3, 3); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
        board.addModifiedData(cell);

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    /**
     * Tests the Touching Corners Direct Rule on a 4x4 board.
     * There is a value tile of 6 in the top-right middle, and a value tile of 7 in the bottom-left middle.
     * The 6-tile has 4 black tiles in range, the 7-tile has 3 black tiles in range.
     * The 7-tile needs to fill the space with 2 black squares and the 6-tile fills its own with white.
     */
    @Test
    public void TouchingCornersDirectRuleTest_4x4Differ7Up2 () throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/fillapix/rules/TouchingCornersDirectRule/4x4Differ7Up2", fillapix );
        TreeNode rootNode = fillapix.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().getFirst();
        transition.setRule(RULE);

        FillapixBoard board = (FillapixBoard) transition.getBoard(); // Reset Board

        // Color (0,2) WHITE
        FillapixCell cell = board.getCell(0, 2); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.WHITE); // 2: WHITE
        board.addModifiedData(cell);

        // Color (2,3) & (3,3) BLACK
        cell = board.getCell(3, 3); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
        board.addModifiedData(cell);

        cell = board.getCell(2, 3); // set the current cell
        Assert.assertNotNull(cell);
        cell.setCellType(FillapixCellType.BLACK); // 1: BLACK
        board.addModifiedData(cell);

        // Test once gone through entire board
        Assert.assertNull(RULE.checkRule(transition));
    }

    

}
