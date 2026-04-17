package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.FinishWithEmptyDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FinishWithEmptyDirectRuleTest {

    public static final FinishWithEmptyDirectRule RULE = new FinishWithEmptyDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    // tests the finish with empty direct rule is many different cases

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected in a basic scenario
    *
    * This is what 3x3test8.txt looks like (e for empty):
    *   e _| e
    *   e 1  e
    *   e #  e
    *
    * This test explicitly changes the top row + center cell from UNSET to EMPTY. The FinishWithEmptyDirectRule
    * should be usable for that cell and none of the others.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyOneNumberTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test8.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected in basic scenario when used on one of many
    * possible valid cells.
    *
    * This is what 3x3test9.txt looks like:
    *   _| _| _|
    *   _| 1  _|
    *      #
    *
    * Now, this test explicitly changes the top row + center cell from UNSET to EMPTY. FinishWithEmptyDirectRule
    * should succeed for that cell, but not for any of the others. If some of the other UNSET cells were changed to
    * EMPTY, the rule would work for them, but since they haven't been set to empty the rule won't work there.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_FiveUnsetOneEmptyOneNumberTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test9.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected when not in the presence of any number, mine, or
    * empty cells.
    *
    * This is what 3x3test.txt looks like:
    *   _| _| _|
    *   _| _| _|
    *   _| _| _|
    *
    * There are only UNSET cells initially; the middle cell is changed to EMPTY. The transition with that change
    * has this rule assigned to it. It verifies that this is recognized as an incorrect use of the rule. It also
    * makes sure that the rule doesn't work on any of the other cells.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_NineUnsetOneEmptyZeroNumbersTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected when not in the presence of any number, mine, or
    * empty cells and used on more than one cell.
    *
    * 3x3test.txt is used again here:
    *   _| _| _|
    *   _| _| _|
    *   _| _| _|
    *
    * This test changes all the cells from UNSET to EMPTY. Again, it checks that the transition is identified as
    * an incorrect use of this rule. It verifies that the rule doesn't work for all the cells individually.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_NineUnsetNineEmptyZeroNumbersTest4()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(1, 0);
        cell2.setData(MinesweeperTileData.empty());
        MinesweeperCell cell3 = board.getCell(2, 0);
        cell3.setData(MinesweeperTileData.empty());
        MinesweeperCell cell4 = board.getCell(0, 1);
        cell4.setData(MinesweeperTileData.empty());
        MinesweeperCell cell5 = board.getCell(1, 1);
        cell5.setData(MinesweeperTileData.empty());
        MinesweeperCell cell6 = board.getCell(2, 1);
        cell6.setData(MinesweeperTileData.empty());
        MinesweeperCell cell7 = board.getCell(0, 2);
        cell7.setData(MinesweeperTileData.empty());
        MinesweeperCell cell8 = board.getCell(1, 2);
        cell8.setData(MinesweeperTileData.empty());
        MinesweeperCell cell9 = board.getCell(2, 2);
        cell9.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);
        board.addModifiedData(cell4);
        board.addModifiedData(cell5);
        board.addModifiedData(cell6);
        board.addModifiedData(cell7);
        board.addModifiedData(cell8);
        board.addModifiedData(cell9);

        Assert.assertNotNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected for a cell that is within the shared bubble
    * region of multiple number cells
    *
    * This is what 3x3test10.txt looks like:
    *      #
    *   1  1  1
    *      _|
    *
    * This test explicitly changes the bottom row + center cell from UNSET to EMPTY. That cell is in the 'domain'
    * of all three of those number cells, and this checks that it satisfies the rule for all three. Or at least it
    * allows the rule to be used in the domain of more than one number cell. This test also makes sure that the rule
    * doesn't work on any of the other cells.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyThreeNumbersTest5()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test10.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 2);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected between two number cells with different values. It
    * must infer when the user is intending it to be used for only one of multiple number cells adjacent to the
    * targeted cell.
    *
    * This is what 3x3test11.txt looks like:
    *   #
    *   2 _| 3
    *   #
    *
    * Now, this test explicitly changes the middle cell from UNSET to EMPTY. It is in the domain of the two and the
    * three at the same time. This would be a valid use of the rule in relation to the two, but not the three. This
    * test verifies that the rule will only look at the two, which already has 2 mines in its bubble. And that it
    * ignores the three, or imagines that the three non-existent cells off the right side of the board hold mines
    * for the three. Because the user, in this scenario the user could not possibly intend this rule to be used
    * for that 'three'.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_OneUnsetOneEmptyThreeNumbersTest6()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test11.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 1);
        cell1.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /*
    *
    * Check that FinishWithEmptyDirectRule behaves as expected when used on two different cells that may or
    * may not exist in overlapping domains.
    *
    * This is what 3x3test12.txt looks like:
    *   _| _| _|
    *   #  #  #
    *   _| 2  _|
    *
    * This test explicitly changes the bottom left and right cells from UNSET to EMPTY. They satisfy this rule's
    * conditions for the bottom row two; the bottom right-hoof cell works for both twos. This verifies that the rule
    * accept both overlapping and non-overlapping number-bubble uses.
    *
    * */
    @Test
    public void FinishWithEmptyDirectRule_FiveUnsetTwoEmptyTwoNumbersTest7()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test12.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 2);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(2, 2);
        cell2.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation()) || point.equals(cell2.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }
}
