package puzzles.minesweeper;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.minesweeper.Minesweeper;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperBoard;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperCell;
import edu.rpi.legup.puzzle.minesweeper.MinesweeperTileData;
import edu.rpi.legup.puzzle.minesweeper.rules.NonTouchingSharedMineDirectRule;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NonTouchingSharedMineDirectRuleTest {

    public static final NonTouchingSharedMineDirectRule RULE =
            new NonTouchingSharedMineDirectRule();
    private static Minesweeper minesweeper;

    @BeforeClass
    public static void setUp() {
        MockGameBoardFacade.getInstance();
        minesweeper = new Minesweeper();
    }

    // Horizontal/vertical tests means the adjacent number cells are adjacent
    // horizontally/vertically

    /**
     * Checks that NonTouchingSharedMineDirectRule behaves as expected. <br>
     * <br>
     * This is what 3x3test13.txt looks like:
     *
     * <pre>
     *   _| _| e
     *   1  2  _|
     *   _| _| e</pre>
     *
     * <br>
     * This test explicitly changes the center cell in the right column from UNSET to MINE. It
     * checks that those adjacent number cells have the same difference in the number of mines in
     * their non-shared bubble regions as the difference in their values. In this case, it is
     * supposed to accept the non-shared bubble region belonging to the '1' cell has zero mines even
     * though it technically doesn't exist since it is off of the board.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_HorizontalTest1()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test13.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(2, 1);
        cell1.setData(MinesweeperTileData.mine());

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
     * Checks that NonTouchingSharedMineDirectRule behaves as expected in larger board scenario.
     * <br>
     * <br>
     * This is what 4x4test1.txt looks like:
     *
     * <pre>
     *  e  _| _| e
     *  e  1  2  _|
     *  e  _| _| e
     *  _| _| _| _|</pre>
     *
     * <br>
     * This test explicitly changes the center cell in the right column from UNSET to MINE. It
     * checks that those adjacent number cells have the same difference in the number of mines in
     * their non-shared bubble regions as the difference in their values.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_HorizontalTest2()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test1.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(3, 1);
        cell1.setData(MinesweeperTileData.mine());

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
     * Checks that NonTouchingSharedMineDirectRule behaves as expected in larger board scenario.
     * <br>
     * <br>
     * This is what 4x4test2.txt looks like:
     *
     * <pre>
     *  _| _| _| e
     *  e  4  2  _|
     *  _| _| _| e
     *  _| _| _| _|</pre>
     *
     * <br>
     * This test explicitly changes the top right corner cell and the cell third from the top in the
     * left column from UNSET to MINE. It verifies that the rule operates correctly to be accepted
     * when the difference in the number of mines across two non-shared bubble regions is the same
     * as the difference in the values of the number cells.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_HorizontalTest3()
            throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test2.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.mine());
        MinesweeperCell cell2 = board.getCell(0, 2);
        cell2.setData(MinesweeperTileData.mine());

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
     * Checks that NonTouchingSharedMineDirectRule behaves as expected. <br>
     * <br>
     * This is what 3x3test13.txt looks like:
     *
     * <pre>
     *   e  _| e
     *   _| 2  _|
     *   _| 1  _|</pre>
     *
     * <br>
     * This test explicitly changes the center cell in the top row from UNSET to MINE. It checks
     * that those adjacent number cells have the same difference in the number of mines in their
     * non-shared bubble regions as the difference in their values. In this case, it is supposed to
     * accept the non-shared bubble region belonging to the '1' cell has zero mines even though it
     * technically doesn't exist since it is off of the board.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_VerticalTest1() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/3x3test14.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 0);
        cell1.setData(MinesweeperTileData.mine());

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
     * Checks that NonTouchingSharedMineDirectRule behaves as expected in larger board scenario.
     * <br>
     * <br>
     * This is what 4x4test2.txt looks like:
     *
     * <pre>
     *   _| e  e  e
     *   _| _| 1  _|
     *   _| _| 4  _|
     *   _| _| _| _|</pre>
     *
     * <br>
     * This test explicitly changes the bottom cell in the second column from the left, the bottom
     * cell in the third column from the left, and the bottom left most cell from UNSET to MINE. It
     * verifies that the rule operates correctly to be accepted when the difference in the number of
     * mines across two non-shared bubble regions is the same as the difference in the values of the
     * number cells.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_VerticalTest2() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/4x4test3.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(1, 3);
        cell1.setData(MinesweeperTileData.mine());
        MinesweeperCell cell2 = board.getCell(2, 3);
        cell2.setData(MinesweeperTileData.mine());
        MinesweeperCell cell3 = board.getCell(3, 3);
        cell3.setData(MinesweeperTileData.mine());

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
     * Checks that NonTouchingSharedMineDirectRule behaves as expected in larger board scenario.
     * <br>
     * <br>
     * This is what NonSharedMine1.txt looks like:
     *
     * <pre>
     *  _| e  e  _|
     *  e  2  _| e
     *  e  _| 1  e
     *  _| e  e  e</pre>
     *
     * <br>
     * This test explicitly changes the top right corner cell UNSET to MINE. It verifies that the
     * rule operates correctly to be accepted when the difference in the number of mines across two
     * non-shared bubble regions is the same as the difference in the values of the number cells.
     */
    @Test
    public void NonTouchingSharedMineDirectRule_HorizontalTest() throws InvalidFileFormatException {
        TestUtilities.importTestBoard("puzzles/minesweeper/rules/NonSharedMine1.txt", minesweeper);
        TreeNode rootNode = minesweeper.getTree().getRootNode();
        TreeTransition transition = rootNode.getChildren().get(0);
        transition.setRule(RULE);

        MinesweeperBoard board = (MinesweeperBoard) transition.getBoard();

        MinesweeperCell cell1 = board.getCell(0, 0);
        cell1.setData(MinesweeperTileData.mine());

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
}
