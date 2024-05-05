package puzzles.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.*;
import edu.rpi.legup.puzzle.treetent.rules.FillinRowCaseRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.util.ArrayList;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FillinRowCaseRuleTest {
    private static final FillinRowCaseRule RULE = new FillinRowCaseRule();
    private static TreeTent treetent;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        treetent = new TreeTent();
    }

    /**
     * empty 3x3 TreeTent puzzle Tests FillinRowCaseRule on row with 3 UNKNOWN tiles and a clue of 0
     * tents in the row.
     *
     * <p>checks that 1 case is created and that it is equivalent to FinishWithGrass rule May need
     * to change checks due to issue #777
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void TentOrTreeTestZeroTentClue() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/EmptyRow3x3ZeroTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        /* Test the Row */
        TreeTentClue testing_row = board.getClue(3, 1);
        ArrayList<Board> cases = RULE.getCases(board, testing_row);

        // assert that one case was found
        Assert.assertEquals(1, cases.size());

        // assert the case filled the row with grass
        TreeTentBoard testCase = (TreeTentBoard) cases.getFirst();
        Assert.assertEquals(3, testCase.getRowCol(1, TreeTentType.GRASS, true).size());

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (h == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);
                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }

        /* Test the Column */
        TreeTentClue testing_col = board.getClue(1, 3);
        cases = RULE.getCases(board, testing_col);

        // assert one case was created
        Assert.assertEquals(1, cases.size());

        // assert the case filled the column with grass
        testCase = (TreeTentBoard) cases.getFirst();
        Assert.assertEquals(3, testCase.getRowCol(1, TreeTentType.GRASS, false).size());

        // checks other cells have not been modified
        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (w == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }
    }

    /**
     * empty 3x3 TreeTent puzzle Tests FillinRowCaseRule on row with 3 UNKNOWN tiles and a clue of 1
     * tent in the row. The column rules make the board impossible, but they are not checked here.
     *
     * <p>checks 3 cases are created checks; first case is TENT tile at x=0, second case is TENT
     * tile at x=1, and a third case is TENT tile at x=2. The cases can be in any order. Then, it
     * checks that other cells have not been modified
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FillInRowEmptyOneTentClue() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/EmptyRow3x3OneTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        /* Test the Row */
        TreeTentClue testing_row = board.getClue(3, 1);
        ArrayList<Board> cases = RULE.getCases(board, testing_row);

        // assert correct number of cases created
        Assert.assertEquals(3, cases.size());

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

            // Each case must have 1 tent in the row
            Assert.assertEquals(1, testCase.getRowCol(1, TreeTentType.TENT, true).size());

            // and they must have 2 grass tiles in the row
            Assert.assertEquals(2, testCase.getRowCol(1, TreeTentType.GRASS, true).size());
        }

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (h == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                for (Board testCaseBoard : cases) {
                    TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

                    case_cell = testCase.getCell(w, h);
                    Assert.assertEquals(original_cell.getType(), case_cell.getType());
                }
            }
        }

        /* Test the Column */
        TreeTentClue testing_col = board.getClue(1, 3);
        cases = RULE.getCases(board, testing_col);

        // assert correct number of cases created
        Assert.assertEquals(3, cases.size());
        // Only one arrangement is possible when taking into account the
        // touching tents contradiction rule.

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

            // Each case must have 1 tent in the column
            Assert.assertEquals(1, testCase.getRowCol(1, TreeTentType.TENT, false).size());

            // and they must have 2 grass tiles in the column
            Assert.assertEquals(2, testCase.getRowCol(1, TreeTentType.GRASS, false).size());
        }

        // checks other cells have not been modified
        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (w == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                for (Board testCaseBoard : cases) {
                    TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

                    case_cell = testCase.getCell(w, h);
                    Assert.assertEquals(original_cell.getType(), case_cell.getType());
                }
            }
        }
    }

    /**
     * empty 3x3 TreeTent puzzle Tests FillinRowCaseRule on row with 3 UNKNOWN tiles and a clue of 2
     * tent in the row. The column rules make the board impossible, but they are not checked here.
     *
     * <p>checks 1 case is created. Checks that the case is when there are TENT tiles at x=0 and
     * x=2. Then, it checks that other cells have not been modified
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FillInRowEmptyTwoTentClue() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/EmptyRow3x3TwoTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        /* Test the Row */
        TreeTentClue testing_row = board.getClue(3, 1);
        ArrayList<Board> cases = RULE.getCases(board, testing_row);

        // assert correct number of cases created
        Assert.assertEquals(1, cases.size());
        // Only one arrangement is possible when taking into account the
        // touching tents contradiction rule.

        TreeTentBoard testCase = (TreeTentBoard) cases.getFirst();

        // The two side tiles are tents,
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(0, 1).getType());
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(2, 1).getType());

        // and the center tile is grass.
        Assert.assertEquals(TreeTentType.GRASS, testCase.getCell(1, 1).getType());

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (h == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }

        /* Test the Column */
        TreeTentClue testing_col = board.getClue(1, 3);
        cases = RULE.getCases(board, testing_col);

        // assert correct number of cases created
        Assert.assertEquals(1, cases.size());
        // Only one arrangement is possible when taking into account the
        // touching tents contradiction rule.

        testCase = (TreeTentBoard) cases.getFirst();

        // The two side tiles are tents,
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(1, 0).getType());
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(1, 2).getType());

        // and the center tile is grass.
        Assert.assertEquals(TreeTentType.GRASS, testCase.getCell(1, 1).getType());

        // checks other cells have not been modified
        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (w == 1) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }
    }

    /**
     * empty 3x3 TreeTent puzzle Tests FillinRowCaseRule on row with 3 UNKNOWN tiles and a clue of 3
     * tent in the row.
     *
     * <p>checks that 0 cases are created
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FillInRowEmptyThreeTentClue() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/EmptyRow3x3ThreeTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        /* Test the Row */
        TreeTentClue testing_row = board.getClue(3, 1);
        ArrayList<Board> cases = RULE.getCases(board, testing_row);

        // assert there were no cases found, as filling in all tiles causes the tents to touch
        Assert.assertNull(cases);

        /* Test the Column */
        TreeTentClue testing_col = board.getClue(1, 3);
        cases = RULE.getCases(board, testing_row);

        Assert.assertNull(cases);
    }

    /**
     * empty 5x5 TreeTent puzzle Tests FillinRowCaseRule on row with 5 UNKNOWN tiles and a clue of 2
     * tents in the row.
     *
     * <p>checks that 6 cases are created and each case has the right number of tents
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FillInRowEmpty5x5TwoTentClue() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/EmptyRow5x5TwoTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();

        // Test the Row
        TreeTentClue testing_row = board.getClue(5, 2);
        ArrayList<Board> cases = RULE.getCases(board, testing_row);

        // assert correct number of cases created
        Assert.assertEquals(6, cases.size());
        // Only one arrangement is possible when taking into account the
        // touching tents contradiction rule.

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

            // Each case must have 2 tens in the row
            Assert.assertEquals(2, testCase.getRowCol(2, TreeTentType.TENT, true).size());

            // and they must have 3 grass tiles in the row
            Assert.assertEquals(3, testCase.getRowCol(2, TreeTentType.GRASS, true).size());
        }

        TreeTentCell original_cell;
        TreeTentCell case_cell;

        // checks other cells have not been modified
        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (h == 2) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                for (Board testCaseBoard : cases) {
                    TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

                    case_cell = testCase.getCell(w, h);
                    Assert.assertEquals(original_cell.getType(), case_cell.getType());
                }
            }
        }

        // Test the Column
        TreeTentClue testing_col = board.getClue(2, 5);
        cases = RULE.getCases(board, testing_col);

        // assert correct number of cases created
        Assert.assertEquals(6, cases.size());
        // Only one arrangement is possible when taking into account the
        // touching tents contradiction rule.

        for (Board testCaseBoard : cases) {
            TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

            // Each case must have 2 tents in the column
            Assert.assertEquals(2, testCase.getRowCol(2, TreeTentType.TENT, false).size());

            // and they must have 4 grass tiles in the column
            Assert.assertEquals(3, testCase.getRowCol(2, TreeTentType.GRASS, false).size());
        }

        // checks other cells have not been modified
        for (int w = 0; w < board.getWidth(); w++) {
            for (int h = 0; h < board.getHeight(); h++) {
                if (w == 2) {
                    continue;
                }

                original_cell = board.getCell(w, h);

                for (Board testCaseBoard : cases) {
                    TreeTentBoard testCase = (TreeTentBoard) testCaseBoard;

                    case_cell = testCase.getCell(w, h);
                    Assert.assertEquals(original_cell.getType(), case_cell.getType());
                }
            }
        }
    }

    /**
     * 7x3 TreeTent puzzle Tests FillinRowCaseRule on col with 3 UNKNOWN tiles separated by grass
     * tiles and a clue of 3 tents in the col.
     *
     * <p>checks that 1 case is created and that all three UNKNOWN tiles have become tents
     *
     * @throws InvalidFileFormatException
     */
    @Test
    public void FillInRowPartialFillThreeTent() throws InvalidFileFormatException {
        TestUtilities.importTestBoard(
                "puzzles/treetent/rules/FillinRowCaseRule/PartialFillOneTent", treetent);
        TreeNode rootNode = treetent.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        TreeTentBoard board = (TreeTentBoard) transition.getBoard();
        TreeTentClue testing_col = board.getClue(1, 7);
        ArrayList<Board> cases = RULE.getCases(board, testing_col);

        // assert that one case was found
        Assert.assertEquals(1, cases.size());

        // assert the case has three tents in the column
        TreeTentBoard testCase = (TreeTentBoard) cases.getFirst();
        Assert.assertEquals(3, testCase.getRowCol(1, TreeTentType.TENT, false).size());

        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(1, 1).getType());
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(1, 3).getType());
        Assert.assertEquals(TreeTentType.TENT, testCase.getCell(1, 5).getType());

        // checks other cells have not been modified
        TreeTentCell original_cell;
        TreeTentCell case_cell;

        for (int w = 0; w < board.getWidth(); w++) {
            if (w == 1) {
                continue;
            }
            for (int h = 0; h < board.getHeight(); h++) {

                original_cell = board.getCell(w, h);
                case_cell = testCase.getCell(w, h);
                Assert.assertEquals(original_cell.getType(), case_cell.getType());
            }
        }
    }
}
