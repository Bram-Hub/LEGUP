package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.NonTouchingSharedEmptyDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NonTouchingSharedEmptyDirectRuleTest {

    public static final NonTouchingSharedEmptyDirectRule RULE =
            new NonTouchingSharedEmptyDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    // Horizontal/vertical tests means the adjacent number cells are adjacent
    // horizontally/vertically

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected for horizontally adjacent number cells
    * with the same value when one of them is against the edge of the board.
    *<br><br>
    * This is what 3x3test15.txt looks like:
    *<pre>
    *   _| _| e
    *   1  1  _|
    *   _| _| e</pre>
    *<br>
    * Now, this test explicitly sets the middle cell in the right column from unset to empty. Those two adjacent
    * numbers need to have the same difference of mine count in their unshared bubble regions as the difference
    * in their values. In this case, that is zero. This test checks that it correctly accepts the number up against
    * the left wall as having zero mines in the region that it doesn't share with its neighbor. The rule should
    * succeed (Y) for the cell that was set to empty and fail (n) for all the others. Like this:
    *<br>
    * <pre>
    *   n| n| n
    *   1  1  Y
    *   n| n| n</pre>
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_HorizontalTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test15.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(2, 1);
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

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected for horizontally adjacent number
    * cells that share the same value.
    *<br><br>
    * This is what 4x4test4.txt looks like:
    *<pre>
    *   _| _| _| e
    *   _| 2  2  e
    *   _| _| _| e
    *   _| _| _| _|</pre>
    *<br>
    * This test explicitly sets the top three cells in the left-hoof column from unset to empty. It verifies
    * that this rule is good (Y) for those three, to satisfy the condition that the number cells have the
    * same mine-count difference in their unshared bubble regions as the difference in their values. And it
    * should fail (n) for other cells. Like this:
    * <br>
    *<pre>
    *   Y  n| n| n
    *   Y  2  2  n
    *   Y  n| n| n
    *   n| n| n| n|</pre>
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_HorizontalTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test4.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(0, 1);
        cell2.setData(MinesweeperTileData.empty());
        MinesweeperCell cell3 = board.getCell(0, 2);
        cell3.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected for vertically adjacent number
    * cells with the same value.
    *<br><br>
    * This is what 4x4test5.txt looks like:
    * <pre>
    *   e  _| _| _|
    *   _| 1  _| _|
    *   _| 1  _| _|
    *   e  e  e  _|</pre>
    *<br>
    * This test explicitly sets the middle two cells on the top row from unset to empty. That fulfills
    * the condition that the two adjacent number cells have the same mine count difference in their
    * unshared bubble regions as the difference in their values. In this case both ought to be zero.
    * It verifies that this rule works (Y) for the top middle two cells and fails (n) for the others.
    * Like this:
    * <br>
    *<pre>
    *   n  Y  Y  n|
    *   n| 1  n| n|
    *   n| 1  n| n|
    *   n  n  n  n|</pre>
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_VerticalTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test5.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(2, 0);
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

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected with vertically adjacent number cells
    * that have different values.
    *<br><br>
    * This is what 4x4test6.txt looks like:
    *<pre>
    *   _| #  e  e
    *   _| _| 3  _|
    *   _| _| 2  _|
    *   _| _| _| _|</pre>
    *<br>
    * This text explicitly sets the right three on the bottom row from unset to empty. Those two number
    * cells have to have the same mine count difference in their unshared bubble regions as the difference
    * in their values, so this rule should work (Y) on those but fail (n) on any of the others. Like this:
    *<pre>
    *   n| #  n  n
    *   n| n| 3  n|
    *   n| n| 2  n|
    *   n| Y  Y  Y</pre>
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_VerticalTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test6.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 3);
        cell1.setData(MinesweeperTileData.empty());
        MinesweeperCell cell2 = board.getCell(2, 3);
        cell2.setData(MinesweeperTileData.empty());
        MinesweeperCell cell3 = board.getCell(3, 3);
        cell3.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                        || point.equals(cell2.getLocation())
                        || point.equals(cell3.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }
    }

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected when two cells are diagonally adjacent.
    *<br><br>
    * This is what NonSharedEmpty1.txt looks like:
    *<pre>
    *   _| e  e  _|
    *   e  1  _| e
    *   e  _| 1  e
    *   _| e  e  e</pre>
    *<br>
    * This test explicitly sets the top-left cell from unset to empty, and on the board it uses it has an
    * empty already in the bottom right. It verifies that the rule works (Y) when used on the top left, and it
    * fails (n) when you try to use it on any of the others. Like this:
    * <br>
    *<pre>
    *   Y  n  n  n|
    *   n  1  n| n
    *   n  n| 1  n
    *   n| n  n  n</pre>
    *<br>
    * note that this rule is only supposed to be used when the player explicitly sets a cell to empty, that's
    * why it is not supposed to work on the bottom right cell.
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_HorizontalTest()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/NonSharedEmpty1.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
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

    /**
    * Check that NonTouchingSharedEmptyDirectRule behaves as expected when two number cells are not directly
    * adjacent. For example:
    *<pre>
    *   #  #  _| _| _|
    *   #  3  _| _| _|
    *   _| _| _| _| _|
    *   _| _| #  3  #
    *   _| _| _| #  _|</pre>
    *<br>
    * Two 3s have one shared cell in their 'bubbles'. And they both already have three mines in the un
    * shared parts of their bubbles. You should be able to use this rule to prove that the shared cell
    * is empty, and it shouldn't work for any other cells. Like this:
    * <pre>
    *   #  #  _| _| _|
    *   #  3  _| _| _|
    *   _| _|    _| _|
    *   _| _| #  3  #
    *   _| _| _| #  _|</pre>
    *<br>
    *   Now, as for this test, it uses a test file for this rule specific for non-adjacent scenarios, an
    *   empty cell within the bubbles of two number cells. The other test only check adjacent scenarios.
    *   This rule should work for only three cells (Y), and should not for any other (n), it looks like:
    *<pre>
    *   2  #  n| n| n| n| n|
    *   #  Y  n| #  n| n| n|
    *   n| n| 3  Y  3  n| n|
    *   n| n| #  #  n| n| n|
    *   n| n| n| n| n| 1  #
    *   n| n| n| n| n| Y  n|
    *   n| n| n| n| #  1  n|</pre>
    * */
    @Test
    public void NonTouchingSharedEmptyDirectRule_DiagonalTest()
        throws InvalidFileFormatException {

        TestUtilities.importTestBoard("puzzles/minesweeper/rules/NotTouchingSharedEmptyDirectRuleTest", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1,1);
        MinesweeperCell cell2 = board.getCell(3,2);
        MinesweeperCell cell3 = board.getCell(5,5);

        cell1.setData(MinesweeperTileData.empty());
        cell2.setData(MinesweeperTileData.empty());
        cell3.setData(MinesweeperTileData.empty());

        board.addModifiedData(cell1);
        board.addModifiedData(cell2);
        board.addModifiedData(cell3);

        Assert.assertNull(RULE.checkRule(transition));

        for (int i = 0; i < board.getHeight(); i++) {
            for (int k = 0; k < board.getWidth(); k++) {
                Point point = new Point(k, i);
                if (point.equals(cell1.getLocation())
                    || point.equals(cell2.getLocation())
                    || point.equals(cell3.getLocation())) {
                    Assert.assertNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                } else {
                    Assert.assertNotNull(RULE.checkRuleAt(transition, board.getCell(k, i)));
                }
            }
        }

    }
}
